export interface LivenessResult {
  valid: boolean;
  codID: string;
  cause: string;
  protocol: string;
  scanResultBlob: string;
}

export interface LivenessSuccessResponse {
  status: 'success';
  result?: LivenessResult;
}

export interface LivenessErrorPayload {
  code: string;
  message: string;
  invalidParam?: string;
}

export interface LivenessErrorResponse {
  status: 'error';
  message: string;
  code?: string;
  invalidParam?: string;
}

export type LivenessResponse = LivenessSuccessResponse | LivenessErrorResponse;
