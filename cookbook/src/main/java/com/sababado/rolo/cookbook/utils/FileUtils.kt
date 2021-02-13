package com.sababado.rolo.cookbook.utils

import java.io.File

object FileUtils {

    private val filesScheduledForDelete = ArrayList<File>()

    /**
     * Number of files that are set to be deleted. Added by [scheduleDelete] and removed by [deleteFiles].
     */
    @JvmStatic
    fun numFilesScheduledForDeletion(): Int {
        return filesScheduledForDelete.size
    }

    /**
     * Schedule a file to be deleted. Files will be deleted when [deleteFiles] is called.
     */
    @JvmStatic
    fun scheduleDelete(file: File?) {
        file?.let {
            filesScheduledForDelete.add(file)
        }
    }

    /**
     * Delete all files set for deletion from [scheduleDelete].
     */
    @JvmStatic
    fun deleteFiles() {
        with(filesScheduledForDelete.iterator()) {
            forEach {
                if (it.exists()) {
                    it.delete()
                }
                remove()
            }
        }

    }
}