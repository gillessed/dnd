export class StatusCodeError extends Error {
    constructor(statusCode) {
        super('Status code error: ' + statusCode);
        this.statusCode = statusCode;
    }
}