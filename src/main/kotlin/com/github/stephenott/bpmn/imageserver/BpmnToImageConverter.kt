package com.github.stephenott.bpmn.imageserver

import java.io.File
import java.io.InputStream
import java.util.concurrent.TimeUnit

class BpmnToImageConverter(val bpmnStream: InputStream,
                           val bpmnFile: File = File.createTempFile("bpmn-file-", ".bpmn"),
                           val imageFile: File = File.createTempFile("bpmn-image-", ".png", bpmnFile.parentFile)) {

    /**
     * Reads the byteStream into the BPMN file
     */
    fun loadBpmnFilefromInputStream(){
        bpmnFile.writeBytes(bpmnStream.readBytes())
    }

    /**
     * Deletes the bpmn file and image file
     */
    fun cleanup(){
        bpmnFile.delete()
        imageFile.delete()
    }

    /**
     * Runs the node cli that converts the BPMN file into a image file
     */
    fun processBpmnToImage(arguments: ConverterArguments?): InputStream {
//        val currentWorkingDir = File("./bpmn-to-image-node-cli/")
        val command = "bpmn-to-image"
//        val cliLoc = "./node_modules/bpmn-to-image/cli" //Within the currentWorkingDir
        val bpmnLoc = bpmnFile.absolutePath
        val imageLoc = imageFile.absolutePath

        val dimensionsArg: String? = arguments?.let {
            if (it.minWidth != null && it.minHeight != null){
                "--min-dimensions=${generateDimensionsString(it.minWidth, it.minHeight)}"
            } else {
                null
            }
        }
        val titleArg: String? = arguments?.title?.let { "--title=$it" }
        val noTitleArg: String? = arguments?.let { if (arguments.noTitle == true) "--no-title" else null }
        val noFooterArg: String? = arguments?.let { if (arguments.noFooter == true) "--no-footer" else null }
        val scaleArg: String? = arguments?.let { "--scale=${arguments.scale}" }

        println("args: $arguments")

        val commandList = listOfNotNull(
                command,
//                cliLoc,
                dimensionsArg,
                titleArg,
                noTitleArg,
                noFooterArg,
                scaleArg,
                "$bpmnLoc:$imageLoc")

        println("list: $commandList")

        val pb = ProcessBuilder(commandList)
                .inheritIO()
//                .directory(currentWorkingDir)
                .start()

        pb.waitFor(10L, TimeUnit.SECONDS)
        println("PB result: ${pb.exitValue()}")
        if (pb.exitValue() != 0){
            throw IllegalStateException("Could not complete the request.")
        }

        println(bpmnLoc)
        println(imageLoc)

        return imageFile.inputStream()
    }

    private fun generateDimensionsString(minWidth: Int?, minHeight: Int?): String?{
        return if (minWidth != null && minHeight != null){
            "${minWidth}x${minHeight}"
        } else {
            null
        }
    }

}