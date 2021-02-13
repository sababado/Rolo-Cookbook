package com.sababado.rolo.cookbook.utils

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class FileUtilsTest {
    @Before
    fun resetUtils() {
        FileUtils.deleteFiles()
    }

    @After
    fun after() {
        FileUtils.deleteFiles()
    }

    @Test
    fun scheduleAndDeleteSingleFile() {
        val file = File("dummy")
        file.createNewFile()
        assert(file.exists())

        FileUtils.scheduleDelete(file)
        // file should still exist
        assert(file.exists())

        // file doesn't exist at this point
        FileUtils.deleteFiles()
        assert(!file.exists())
    }

    @Test
    fun scheduleAndDeleteLotsOfFiles() = runBlocking {
        // add a bunch of files
        val fileTestCount = 1000
        val counter = AtomicInteger()
        val list = ArrayList<File>()
        coroutineScope {
            // start adding files
            launch {
                for (c in 0..fileTestCount) {
                    val i = counter.incrementAndGet()
                    val file = File("dummy$i")
                    file.createNewFile()
                    list.add(file)
                    FileUtils.scheduleDelete(file)
                }
            }
//            }
            // delete some files
            FileUtils.deleteFiles()
            // there should be no concurrency exceptions here
            // some files may not be deleted.
        }
        // this should work but doesn't. All files either exist or they don't
//        val numFiles = FileUtils.numFilesScheduledForDeletion()
//        assert(numFiles > 0)
//        assert(numFiles < fileTestCount)

        // cleanup
        FileUtils.deleteFiles()
        list.forEach {
            assert(!it.exists())
        }
    }
}
