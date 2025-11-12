export interface LivenessResult {
  valid: boolean;
  codID: string;
  cause: string;
  protocol: string;
  scanResultBlob: string;
}

export interface LivenessSuccessResponse {
  status: 'success';
  result: LivenessResult;
}

export interface LivenessErrorResponse {
  status: 'error';
  message: string;
}

export type LivenessResponse = LivenessSuccessResponse | LivenessErrorResponse;
