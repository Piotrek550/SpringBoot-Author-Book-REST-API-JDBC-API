package com.example.frontend;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;

import com.example.frontend.model.Author;
import com.example.frontend.model.Book;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonExporter {

    private static final String TAG = "JsonExporter";
    private static final String CHANNEL_ID = "export_channel";
    private static final String CHANNEL_NAME = "Export Notifications";

    public static void exportAuthorsToJson(Context context, List<Author> authors) {
        // Obiekt Gson do serializacji
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Serializacja autorów do JSON
        String jsonContent = gson.toJson(authors);

        // SCIEZKA
        File exportDir = new File(context.getExternalFilesDir(null), "/ExportedData/ExportedAuthors");
        if (!exportDir.exists() && !exportDir.mkdirs()) {
            Toast.makeText(context, "Failed to create export directory!", Toast.LENGTH_SHORT).show();
            return;
        }

        // PLIK
        File jsonFile = new File(exportDir, "authors.json");
        String notificationTitle = "Authors exported successfully";
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(jsonContent);
            showNotification(context, notificationTitle, jsonFile.getAbsolutePath());
            //Toast.makeText(context, "Exported to " + jsonFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "JSON exported to: " + jsonFile.getAbsolutePath());
        } catch (IOException e) {
            Toast.makeText(context, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error exporting JSON", e);
        }
    }

    public static void exportBooksToJson(Context context, List<Book> books) {
        // Obiekt Gson do serializacji
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Serializacja autorów do JSON
        String jsonContent = gson.toJson(books);

        // SCIEZKA
        File exportDir = new File(context.getExternalFilesDir(null), "/ExportedData/ExportedBooks");
        if (!exportDir.exists() && !exportDir.mkdirs()) {
            Toast.makeText(context, "Failed to create export directory!", Toast.LENGTH_SHORT).show();
            return;
        }

        // PLIK
        File jsonFile = new File(exportDir, "books.json");
        String notificationTitle = "Books exported successfully";
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(jsonContent);
            showNotification(context, notificationTitle, jsonFile.getAbsolutePath());
            //Toast.makeText(context, "Exported to " + jsonFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "JSON exported to: " + jsonFile.getAbsolutePath());
        } catch (IOException e) {
            Toast.makeText(context, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error exporting JSON", e);
        }
    }

    private static void showNotification(Context context, String title, String filePath) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tworzenie kanalu powiadomien
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications for export operations");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Budowanie powiadomienia
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setContentTitle(title)
                .setContentText("File saved at: " + filePath)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Wyświetlanie powiadomienia
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
}
