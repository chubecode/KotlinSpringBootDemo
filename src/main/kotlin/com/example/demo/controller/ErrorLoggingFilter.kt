package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.UnsupportedEncodingException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class ErrorLoggingFilter : OncePerRequestFilter() {
    companion object {
        private val logger = LoggerFactory.getLogger(ErrorLoggingFilter::class.java)
        private const val MAX_PAYLOAD_LENGTH = 1000
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val cachedRequest = ContentCachingRequestWrapper(request)
        val cachedResponse = ContentCachingResponseWrapper(response)

        try {
            filterChain.doFilter(cachedRequest, cachedResponse)
        } finally {
            if (response.status == HttpStatus.OK.value() && request.getHeader("a") == "L") {
                val requestBody = getRequestPayload(cachedRequest)
                logger.error("Error in request. Status: ${response.status}, Method: ${request.method}, URL: ${request.requestURI}, Payload: $requestBody")
            }
            cachedResponse.copyBodyToResponse()
        }
    }

    private fun getRequestPayload(request: ContentCachingRequestWrapper): String {
        val payload = request.contentAsByteArray
        val length = payload.size.coerceAtMost(MAX_PAYLOAD_LENGTH)
        try {
            return String(payload, 0, length, charset(request.characterEncoding))
                .replace("\"password\":\"[^\"]*\"".toRegex(), "\"password\":\"[FILTERED]\"")
        } catch (e: UnsupportedEncodingException) {
            return "Unsupported Encoding"
        }
    }
}