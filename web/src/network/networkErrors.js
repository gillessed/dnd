export class StatusCodeError extends Error {
    constructor({statusCode, errorObject}) {
        super('Status code error: ' + statusCode);
        this.statusCode = statusCode;
        this.errorObject = errorObject;
    }
}
