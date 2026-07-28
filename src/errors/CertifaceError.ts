import type { LivenessErrorPayload } from '../@types/result';

export class CertifaceError extends Error {
  readonly code: string;
  readonly invalidParam?: string;

  constructor(code: string, message: string, invalidParam?: string) {
    super(message);
    this.name = 'CertifaceError';
    this.code = code;
    this.invalidParam = invalidParam;
  }

  static fromPayload(payload: LivenessErrorPayload): CertifaceError {
    return new CertifaceError(
      payload.code,
      payload.message,
      payload.invalidParam
    );
  }
}
