package com.sinif.akilzeka;

import android.app.Activity;
import android.content.Intent;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import java.io.OutputStream;

@CapacitorPlugin(name = "SaveFile")
public class SaveFilePlugin extends Plugin {
    @PluginMethod
    public void save(PluginCall call) {
        Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType(call.getString("mime", "application/octet-stream"));
        i.putExtra(Intent.EXTRA_TITLE, call.getString("name", "yedek.json"));
        startActivityForResult(call, i, "done");
    }

    @ActivityCallback
    private void done(PluginCall call, ActivityResult r) {
        if (call == null) return;
        if (r.getResultCode() != Activity.RESULT_OK || r.getData() == null || r.getData().getData() == null) {
            call.reject("iptal");
            return;
        }
        try (OutputStream o = getContext().getContentResolver().openOutputStream(r.getData().getData())) {
            o.write(call.getString("data", "").getBytes("UTF-8"));
            call.resolve();
        } catch (Exception e) {
            call.reject(String.valueOf(e.getMessage()));
        }
    }
}
