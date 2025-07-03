@file:Suppress("OPT_IN_IS_NOT_ENABLED")   // для ExperimentalMaterial3Api

package com.example.litera.screen

/* ---------- imports ОБЯЗАТЕЛЬНО в начале файла ---------- */

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
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
import androidx.compose.ui.tooling.preview.Preview
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
import com.positiondev.epublib.EpubReader
import com.kursx.parser.fb2.FB2Reader
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.FileInputStream
import kotlin.io.path.extension

/* ---------- модель ---------- */
data class Book(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val coverUrl: String = "",
    val totalPages: Int = 0,
    val currentPage: Int = 0,
    val driveId: String = "",
    val localPath: String = ""
) {
    val progress get() = if (totalPages == 0) 0 else currentPage * 100 / totalPages
    fun shortTitle(max: Int = 24): String =
        if (title.length <= max) title else title.take(max - 1).trimEnd() + "…"
}

/* ---------- экран ---------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {

    val ctx    = LocalContext.current
    val auth   = remember { FirebaseAuth.getInstance() }
    val uid    = auth.currentUser?.uid ?: return                       // гость
    val db     = remember { FirebaseFirestore.getInstance() }
    val colRef = remember { db.collection("users").document(uid).collection("books") }
    val driveH = remember { DriveServiceHelper(ctx) }

    /* поток книг */
    val books by produceState(initialValue = emptyList<Book>()) {
        val l = colRef.addSnapshotListener { snap, _ ->
            value = snap?.documents?.mapNotNull { it.toObject(Book::class.java)?.copy(id = it.id) }
                ?: emptyList()
        }
        awaitDispose { l.remove() }
    }

    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    /* long-tap меню */
    var chosenBook  by remember { mutableStateOf<Book?>(null) }
    var sheetOpen   by remember { mutableStateOf(false) }
    var renameDlg   by remember { mutableStateOf(false) }
    var newTitle    by remember { mutableStateOf("") }

    /* SAF-пикер */
    val picker = rememberLauncherForActivityResult(OpenDocument()) { uri: Uri? ->
        if (uri != null) scope.launch(Dispatchers.IO) {
            uploadBook(ctx, uri, driveH, colRef, snack)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text ="Библиотека", fontSize =  28.sp, fontWeight =  FontWeight.Bold, color = Color(0xFF2C3E50))
                },
                actions = {
                    IconButton(
                        onClick = {
                            picker.launch(
                                arrayOf(
                                    "application/epub+zip",
                                    "application/*fb2*",
                                    "application/pdf"
                                )
                            )
                        }
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color(0xFF2C3E50))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { inner ->

        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FDF5))
                .padding(inner)
        ) {
            CollectionsRow { navController.navigate(Screen.Collections.route) }
            Spacer(Modifier.height(16.dp))
            BooksGrid(
                books = books,
                onLongPress = { b ->
                    chosenBook = b
                    newTitle   = b.title
                    sheetOpen  = true
                }
            )
        }
    }

    /* bottom-sheet с действиями */
    if (sheetOpen && chosenBook != null) {
        ModalBottomSheet(onDismissRequest = { sheetOpen = false }) {
            ListItem(
                headlineContent = { Text("Добавить в коллекцию") },
                leadingContent  = { Icon(Icons.Default.CollectionsBookmark, null) },
                modifier = Modifier.clickable {
                    sheetOpen = false
                    navController.navigate(Screen.Collections.route)
                }
            )
            ListItem(
                headlineContent = { Text("Переименовать") },
                leadingContent  = { Icon(Icons.Default.Edit, null) },
                modifier = Modifier.clickable {
                    sheetOpen = false
                    renameDlg = true
                }
            )
            ListItem(
                headlineContent = { Text("Удалить", color = Color.Red) },
                leadingContent  = { Icon(Icons.Default.Delete, null, tint = Color.Red) },
                modifier = Modifier.clickable {
                    sheetOpen = false
                    scope.launch(Dispatchers.IO) {
                        deleteBook(chosenBook!!, driveH, colRef, snack)
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
        }
    }

    /* диалог переименования */
    if (renameDlg && chosenBook != null) {
        AlertDialog(
            onDismissRequest = { renameDlg = false },
            confirmButton = {
                TextButton(onClick = {
                    renameDlg = false
                    scope.launch {
                        colRef.document(chosenBook!!.id).update("title", newTitle).await()
                        snack.showSnackbar("Переименовано")
                    }
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { renameDlg = false }) { Text("Отмена") } },
            title = { Text("Переименовать") },
            text  = {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    singleLine = true
                )
            }
        )
    }
}

/* ---------- GRID & карточки ---------- */

@Composable
private fun BooksGrid(books: List<Book>, onLongPress: (Book) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement   = Arrangement.spacedBy(16.dp)
    ) {
        items(books, key = { it.id }) { book ->
            BookCard(book, onLongPress)
        }
    }
}

/* ============== карточка ============== */
@Composable
private fun BookCard(book: Book, onLongPress: (Book) -> Unit) {
    Column(
        Modifier.combinedClickable(
            onClick = {},
            onLongClick = { onLongPress(book) }
        )
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            if (book.coverUrl.isNotBlank()) {
                AsyncImage(
                    model = book.coverUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            book.shortTitle(),
            fontWeight = FontWeight.SemiBold,
            fontSize   = 14.sp,
            color      = Color(0xFF2C3E50)
        )
        if (book.author.isNotBlank()) {
            Text(book.author, fontSize = 12.sp, color = Color.Gray)
        }
        Text("${book.progress}% •", fontSize = 12.sp, color = Color.Gray)
    }
}

/* ---------- строка “Коллекции” ---------- */

@Composable
private fun CollectionsRow(onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(Color(0xFFF8FDF5))) {
        Divider(thickness = 1.dp, color = Color.LightGray)
        Row(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Menu, null, tint = Color(0xFF2C3E50))
            Spacer(Modifier.width(8.dp))
            Text(text ="Коллекции", fontSize =  16.sp, color = Color(0xFF2C3E50), modifier = Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowRight, null, tint = Color(0xFF2C3E50))
        }
        Divider(thickness =  1.dp, color =  Color.LightGray)
    }
}

/* ============== Upload: используем meta.title / meta.author ============== */
private suspend fun uploadBook(
    ctx: Context,
    uri: Uri,
    drive: DriveServiceHelper,
    col: CollectionReference,
    snack: SnackbarHostState
) = withContext(Dispatchers.IO) {

    val fileName = uri.displayName(ctx.contentResolver)
    val dst      = ctx.filesDir.resolve("books").apply { mkdirs() }.resolve(fileName)

    ctx.contentResolver.openInputStream(uri)!!.use { inp ->
        dst.outputStream().use { inp.copyTo(it) }
    }

    /* --- META-ДАННЫЕ из файла --- */
    val meta = parseBookMeta(dst)      // title / author / pages / coverBytes?

    val mime    = ctx.contentResolver.getType(uri) ?: "application/epub+zip"
    val driveId = drive.upload(dst, meta.title, mime)

    col.add(
        Book(
            title       = meta.title,
            author      = meta.author,
            totalPages  = meta.totalPages,
            driveId     = driveId,
            localPath   = dst.absolutePath
        )
    ).await()

    withContext(Dispatchers.Main) {
        snack.showSnackbar("Книга «${meta.title}» добавлена")
    }
}

/* ============== META-парсер (заглушка) ============== */
private data class Meta(
    val title: String,
    val author: String,
    val totalPages: Int
)

/* ─── извлекаем meta для EPUB / FB2 / PDF ─── */
private fun parseBookMeta(file: File): Meta = when (file.extension.lowercase()) {

    /* ---------- EPUB ---------- */
    "epub" -> {
        val book  = EpubReader().readEpub(FileInputStream(file))
        val title = book.title ?: file.nameWithoutExtension
        val author = book.metadata.authors
            .joinToString { it.firstname + " " + it.lastname }
            .ifBlank { "" }

        // epublib даёт количество секций; грубая оценка страниц:
        val pages = (book.spine.spineReferences.size * 2.3f).toInt().coerceAtLeast(1)
        Meta(title, author, pages)
    }

    /* ---------- FB2 ---------- */
    "fb2" -> {
        val reader = FB2Reader.parse(file)          // DOM-парсинг
        val desc   = reader.description
        val title  = desc?.titleInfo?.bookTitle ?: file.nameWithoutExtension
        val author = desc?.titleInfo?.authors
            ?.joinToString { it.firstName + " " + it.lastName }
            ?.ifBlank { "" } ?: ""

        // у FB2 нет «страниц» — берём объём файла /2КБ как приблизительные страницы
        val pages  = (file.length() / 2048L).toInt().coerceAtLeast(1)
        Meta(title, author, pages)
    }

    /* ---------- PDF ---------- */
    "pdf" -> {
        PDDocument.load(file).use { pdf ->
            Meta(
                title  = pdf.documentInformation?.title ?: file.nameWithoutExtension,
                author = pdf.documentInformation?.author ?: "",
                totalPages = pdf.numberOfPages.coerceAtLeast(1)
            )
        }
    }

    /* ---------- неизвестный формат ---------- */
    else -> Meta(
        title       = file.nameWithoutExtension,
        author      = "",
        totalPages  = 300
    )
}

private suspend fun deleteBook(
    book: Book,
    drive: DriveServiceHelper,
    col: CollectionReference,
    snack: SnackbarHostState
) = withContext(Dispatchers.IO) {
    kotlin.runCatching {
        if (book.driveId.isNotBlank()) drive.deleteFile(book.driveId)
        File(book.localPath).delete()
        col.document(book.id).delete().await()
    }.onSuccess {
        withContext(Dispatchers.Main) { snack.showSnackbar("Удалено") }
    }.onFailure { e ->
        withContext(Dispatchers.Main) { snack.showSnackbar(e.localizedMessage ?: "Ошибка удаления") }
    }
}

private suspend fun syncFromDrive(
    ctx: Context,
    drive: DriveServiceHelper,
    col: CollectionReference
) = withContext(Dispatchers.IO) {
    col.get().await().forEach { doc ->
        val b = doc.toObject(Book::class.java)
        val f = File(b.localPath)
        if (!f.exists() && b.driveId.isNotBlank()) {
            f.parentFile?.mkdirs()
            drive.download(b.driveId, f)
            doc.reference.update("localPath", f.absolutePath)
        }
    }
}

/* ---------- util ---------- */

private fun Uri.displayName(cr: ContentResolver): String {
    var name = lastPathSegment ?: "book_${System.currentTimeMillis()}"
    cr.query(this, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
        if (c.moveToFirst()) {
            c.getString(c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))?.let { name = it }
        }
    }
    return name
}

private data class ParsedBook(val title: String, val totalPages: Int)

/* ---------- extension к DriveServiceHelper ---------- */

suspend fun DriveServiceHelper.deleteFile(id: String) =
    withContext(Dispatchers.IO) { driveService().files().delete(id).execute() }

/* ---------- Preview ---------- */
@Preview(showBackground = true, device = "id:pixel_6")
@Composable private fun LibPrev() {
    LibraryScreen(rememberNavController())
}
