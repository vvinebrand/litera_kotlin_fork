package com.example.litera.drive

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import java.io.File as IoFile

/**
 * Обёртка вокруг Google Drive REST v3.
 *
 * Требует, чтобы пользователь уже прошёл Google Sign-In с
 * запрошенным scope `DriveScopes.DRIVE_FILE`.
 */
class DriveServiceHelper(private val context: Context) {

    /* кешируем экземпляр в памяти, чтобы не создавать каждый раз */
    private var cached: Drive? = null

    /** Получаем авторизованный Drive-клиент. */
    suspend fun driveService(): Drive {
        cached?.let { return it }

        /* пробуем взять уже залогиненный аккаунт */
        val account = GoogleSignIn.getLastSignedInAccount(context)
            ?: error("GoogleSignInAccount == null — вызовите signIn в UI и повторите")

        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_FILE)
        ).apply { selectedAccount = account.account }

        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("Litera").build()
            .also { cached = it }
    }

    /* ---------- API ---------- */

    /** Загружаем локальный файл в Drive, возвращаем fileId */
    suspend fun upload(local: IoFile, title: String, mime: String): String {
        val drive = driveService()
        val meta  = File().apply { name = title }
        return drive.files()
            .create(meta, FileContent(mime, local))
            .setFields("id")
            .execute()
            .id
    }

    /** Скачиваем файл по id в dst */
    suspend fun download(fileId: String, dst: IoFile) {
        val drive = driveService()
        drive.files().get(fileId).executeMediaAndDownloadTo(dst.outputStream())
    }

    companion object {

        /**
         * Запускает экран выбора аккаунта + OAuth-разрешение.
         * Вызывать из UI (Activity / Composable) один раз,
         * потом `DriveServiceHelper` будет пользоваться сохранённым аккаунтом.
         */
        fun signInClient(context: Context): GoogleSignInClient {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()
            return GoogleSignIn.getClient(context, gso)
        }
    }
}
