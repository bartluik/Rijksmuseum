package it.luik.rijksmuseum.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class CoroutinesTestExtension : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}
