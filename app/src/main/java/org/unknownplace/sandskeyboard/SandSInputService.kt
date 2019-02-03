package org.unknownplace.sandskeyboard

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent

class SandSInputService : InputMethodService() {

    var spacePressed = false
    var sandsFired = false

    override fun onCreate() {
        super.onCreate()
        spacePressed = false
        sandsFired = false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val event = event ?: return super.onKeyDown(keyCode, event)

        if (keyCode == 62) {
            spacePressed = true
            return true
        }

        if (spacePressed) {
            sandsFired = true

            val ic = currentInputConnection ?: return false

            val t = System.currentTimeMillis()
            ic.sendKeyEvent(KeyEvent(t, t, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0))
            ic.sendKeyEvent(KeyEvent(t, t, KeyEvent.ACTION_DOWN, keyCode, 0, event.metaState or KeyEvent.META_SHIFT_LEFT_ON))

            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val event = event ?: return super.onKeyUp(keyCode, event)

        if (keyCode == 62) {
            if (sandsFired) {
                spacePressed = false
                sandsFired = false
                return true
            } else {
                spacePressed = false
                sandsFired = false

                val ic = currentInputConnection ?: return false

                val t = System.currentTimeMillis()
                ic.sendKeyEvent(KeyEvent(t, t, KeyEvent.ACTION_DOWN, keyCode, 0, event.metaState))
                ic.sendKeyEvent(KeyEvent(t, t, KeyEvent.ACTION_UP, keyCode, 0, event.metaState))

                return true
            }
        }

        if (spacePressed) {
            val ic = currentInputConnection ?: return false

            val t = System.currentTimeMillis()
            ic.sendKeyEvent(KeyEvent(t, t, KeyEvent.ACTION_UP, keyCode, 0,event.metaState or KeyEvent.META_SHIFT_LEFT_ON))
            ic.sendKeyEvent(KeyEvent(t, t, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SHIFT_LEFT, 0, event.metaState))

            return true
        }

        return super.onKeyUp(keyCode, event)
    }
}