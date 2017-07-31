package com.mypersonalupdates.webserver.rest;

import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

/**
 * Esta clase representa una petición recibida
 * por la API REST.
 */
public final class RESTRequest extends Request {
    private final UriInfo uriInfo;
    private final HttpHeaders httpHeaders;

    private RESTRequest(UriInfo uriInfo, HttpHeaders headers, String JSONBody) throws RequestProcessingException, NullPointerException {
        super(
                uriInfo == null ? null : uriInfo.getPathParameters(),
                uriInfo == null ? null : uriInfo.getQueryParameters(),
                headers == null ? null : headers.getRequestHeaders(),
                JSONBody
        );

        if (uriInfo == null)
            throw new NullPointerException("Parameter uriInfo cannot be null.");

        this.uriInfo = uriInfo;
        this.httpHeaders = headers;
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public static final class Builder {
        private UriInfo uriInfo = null;
        private HttpHeaders httpHeaders = null;
        private String body = null;
        private boolean canBeEmpty = false;

        public Builder setUriInfo(UriInfo uriInfo) {
            this.uriInfo = uriInfo;
            return this;
        }

        public Builder setHeaders(HttpHeaders headers) {
            this.httpHeaders = headers;
            return this;
        }

        public Builder setCanBeEmpty(boolean canBeEmpty) {
            this.canBeEmpty = canBeEmpty;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public RESTRequest build() throws RequestProcessingException {
            RESTRequest ret = new RESTRequest(
                    this.uriInfo,
                    this.httpHeaders,
                    this.body
            );

            ret.setCanBeEmpty(this.canBeEmpty);

            return ret;
        }
    }
}
