package com.digel.cvbuilder;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.content.Context;
import androidx.activity.OnBackPressedCallback;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WebView webView = this.bridge.getWebView();
        
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void printPage() {
                runOnUiThread(() -> {
                    PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                    PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter("CV_Resume");
                    
                    // Ορισμός A4 μεγέθους και ZERO margins ώστε να μην κόβονται οι σελίδες
                    PrintAttributes attributes = new PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setMinMargins(PrintAttributes.Margins.ZERO)
                        .build();

                    printManager.print("CV_Resume_Job", printAdapter, attributes);
                });
            }
        }, "AndroidPrint");

        // Χειρισμός του κουμπιού Back του Android
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                webView.evaluateJavascript("handleAndroidBack()", value -> {
                    if ("false".equals(value) || value == null || "null".equals(value)) {
                        finish();
                    }
                });
            }
        });
    }
}
