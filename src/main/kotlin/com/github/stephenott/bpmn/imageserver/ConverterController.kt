package com.github.stephenott.bpmn.imageserver

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.http.server.types.files.StreamedFile
import io.reactivex.Single
import javax.validation.Valid

@Controller("/")
class ConverterController() {

    @Post(value = "/{?arguments*}", consumes = [MediaType.MULTIPART_FORM_DATA])
    fun upload(file: CompletedFileUpload, @QueryValue arguments: ConverterArguments): Single<HttpResponse<StreamedFile>> {
        val converter = BpmnToImageConverter(file.inputStream)

        return Single.fromCallable {
            converter.loadBpmnFilefromInputStream()
        }.map {
            converter.processBpmnToImage(arguments)

        }.onErrorResumeNext {
            Single.error(IllegalArgumentException(it))
        }.doFinally {
            converter.cleanup()
            println("Post-Completion Cleanup Complete")

        }.map {
            HttpResponse.ok(StreamedFile(it, MediaType.IMAGE_PNG_TYPE))
        }
    }

    @Post(value = "/{?arguments*}", consumes = [MediaType.APPLICATION_XML])
    fun xmlSubmit(@Body xml: Single<String>, arguments: ConverterArguments?): Single<HttpResponse<StreamedFile>> {
        val converter = BpmnToImageConverter(xml.blockingGet().byteInputStream())

        return Single.fromCallable {
            converter.loadBpmnFilefromInputStream()
        }.map {
            converter.processBpmnToImage(arguments)

        }.onErrorResumeNext {
            Single.error(IllegalArgumentException(it))
        }.doFinally {
            converter.cleanup()
            println("Post-Completion Cleanup Complete")

        }.map {
            HttpResponse.ok(StreamedFile(it, MediaType.IMAGE_PNG_TYPE))
        }
    }

    @Error
    fun lintingError(request: HttpRequest<*>, exception: Exception): HttpResponse<Any> {
        exception.printStackTrace()
        return HttpResponse.badRequest("123")
    }

}