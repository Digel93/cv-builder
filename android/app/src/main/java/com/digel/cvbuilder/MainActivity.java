package com.digel.cvbuilder;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.content.Context;
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
                    printManager.print("CV_Resume_Job", printAdapter, new PrintAttributes.Builder().build());
                });
            }
        }, "AndroidPrint");
    }
}
