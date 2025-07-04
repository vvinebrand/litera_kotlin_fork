@file:Suppress("OPT_IN_IS_NOT_ENABLED") // ExperimentalMaterial3Api
package com.example.litera.screen

import android.content.ContentResolver
import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.litera.drive.DriveServiceHelper
import com.example.litera.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.File
import android.provider.OpenableColumns
import androidx.compose.foundation.clickable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.ContentScale

/* ─────────────── модель ─────────────── */
data class Book(
    val id: String = "",
    val title: String = "",     //= имя файла
    val driveId: String = "",
    val localPath: String = ""
)

/* ─────────────── LibraryScreen ─────────────── */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {

    val ctx = LocalContext.current
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val booksCol = remember {
        FirebaseFirestore.getInstance()
            .collection("users").document(uid).collection("books")
    }
    val drive = remember { DriveServiceHelper(ctx) }

    /* live-flow из Firestore */
    val books by produceState(listOf<Book>()) {
        val l = booksCol.addSnapshotListener { s, _ ->
            value = s?.documents?.mapNotNull { it.toObject(Book::class.java)?.copy(id = it.id) }
                ?: emptyList()
        }
        awaitDispose { l.remove() }
    }

    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    /* SAF picker */
    val launcher = rememberLauncherForActivityResult(OpenDocument()) { uri: Uri? ->
        if (uri != null) scope.launch(Dispatchers.IO) {
            uploadBook(ctx, uri, drive, booksCol, snack)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        topBar = {
            TopAppBar(
                title = { Text("Библиотека", 28.sp, FontWeight.Bold, Color(0xFF2C3E50)) },
                actions = {
                    IconButton(
                        onClick = {
                            launcher.launch(
                                arrayOf(
                                    "application/epub+zip",
                                    "application/*fb2*",
                                    "application/pdf"
                                )
                            )
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFF2C3E50)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { inner ->

        Column(Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FDF5))
            .padding(inner)) {

            /* ▼ “Коллекции” строка */
            CollectionsRow { navController.navigate(Screen.Collections.route) }

            Spacer(Modifier.height(16.dp))

            /* ▼ Сетка книг */
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement   = Arrangement.spacedBy(16.dp)
            ) {
                items(books, key = Book::id) { book ->
                    BookCard(book,
                        onClick     = { navController.navigate("reader/${book.localPath}") },
                        onLongClick = { /* TODO: sheet / rename / delete */ }
                    )
                }
            }
        }
    }
}

/* ─────────────── одна карточка ─────────────── */
@Composable
private fun BookCard(
    book: Book,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Column(Modifier.combinedClickable(onClick, onLongClick)) {
        Box(Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .background(Color.LightGray, RoundedCornerShape(8.dp)))
        Spacer(Modifier.height(8.dp))
        Text(book.title, 14.sp, FontWeight.SemiBold, maxLines = 2)
    }
}

/* ─────────────── строка “Коллекции” ─────────────── */
@Composable
private fun CollectionsRow(onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(Color(0xFFF8FDF5))) {
        Divider(1.dp, Color.LightGray)
        Row(Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Menu, null, tint = Color(0xFF2C3E50))
            Spacer(Modifier.width(8.dp))
            Text("Коллекции", 16.sp, color = Color(0xFF2C3E50), modifier = Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowRight, null, tint = Color(0xFF2C3E50))
        }
        Divider(1.dp, Color.LightGray)
    }
}

/* ─────────────── Upload без парсеров ─────────────── */
private suspend fun uploadBook(
    ctx: Context,
    uri: Uri,
    drive: DriveServiceHelper,
    col: CollectionReference,
    snack: SnackbarHostState
) = withContext(Dispatchers.IO) {
    val fileName = uri.displayName(ctx.contentResolver)
    val dst = ctx.filesDir.resolve("books").apply { mkdirs() }.resolve(fileName)

    ctx.contentResolver.openInputStream(uri)!!.use { input ->
        dst.outputStream().use { input.copyTo(it) }
    }

    val driveId = drive.upload(dst, fileName, ctx.contentResolver.getType(uri) ?: "application/octet-stream")

    col.add(Book(title = fileName, driveId = driveId, localPath = dst.absolutePath)).await()

    withContext(Dispatchers.Main) { snack.showSnackbar("Добавлено: $fileName") }
}

/* helper */
private fun Uri.displayName(cr: ContentResolver): String =
    cr.query(this, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        ?.use { if (it.moveToFirst()) it.getString(0) else null }
        ?: lastPathSegment ?: "file_${System.currentTimeMillis()}"

/* ──────────────────────────────────────────────── */
/*                Ч И Т А Л К А                    */
/* ──────────────────────────────────────────────── */

@Composable
fun BookReaderScreen(path: String) {
    val ctx = LocalContext.current
    val file = remember(path) { File(path) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(file.nameWithoutExtension) })
        }
    ) { inner ->

        when (file.extension.lowercase()) {
            /* -------- PDF -------- */
            "pdf" -> PdfPagePreview(file, Modifier.padding(inner))

            /* -------- другие (заглушка) -------- */
            else  -> Box(Modifier
                .fillMaxSize()
                .padding(inner),
                contentAlignment = Alignment.Center) {
                Text("📖 Чтение файла: ${file.name}", fontSize = 18.sp)
            }
        }
    }
}

/* очень простой single-page PDF-preview */
@Composable
private fun PdfPagePreview(file: File, modifier: Modifier = Modifier) {
    val bmpState = rememberSaveable { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(file) {
        withContext(Dispatchers.IO) {
            try {
                val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                PdfRenderer(pfd).use { renderer ->
                    renderer.openPage(0).use { page ->
                        val bmp = android.graphics.Bitmap.createBitmap(
                            page.width, page.height, android.graphics.Bitmap.Config.ARGB_8888)
                        page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        bmpState.value = bmp
                    }
                }
            } catch (_: Exception) { }
        }
    }

    bmpState.value?.let { bitmap ->
        androidx.compose.foundation.Image(
            bitmap = androidx.compose.ui.graphics.asImageBitmap(bitmap),
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    } ?: Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

/* ───────────────── Preview ───────────────── */
@Preview(showBackground = true, device = "id:pixel_6")
@Composable private fun LibPrev() {
    LibraryScreen(rememberNavController())
}