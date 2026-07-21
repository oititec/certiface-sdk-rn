import type { LivenessErrorPayload } from '../@types/result';
import { CertifaceError } from './CertifaceError';

const LEGACY_BRACKET_PATTERN = /^\[([^\]]+)\]:\s*(.*)$/;

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === 'object' && value !== null;
}

function readOptionalString(
  record: Record<string, unknown>,
  key: string
): string | undefined {
  const value = record[key];
  return typeof value === 'string' && value.length > 0 ? value : undefined;
}

export function parseNativeError(raw: string): CertifaceError {
  try {
    const parsed: unknown = JSON.parse(raw);
    if (isRecord(parsed)) {
      const code =
        readOptionalString(parsed, 'code') ??
        readOptionalString(parsed, 'errorType');
      const message =
        readOptionalString(parsed, 'message') ??
        readOptionalString(parsed, 'errorMessage');
      const invalidParam = readOptionalString(parsed, 'invalidParam');

      if (code && message) {
        return CertifaceError.fromPayload({
          code,
          message,
          invalidParam,
        });
      }

      if (message) {
        return CertifaceError.fromPayload({
          code: 'UNKNOWN_ERROR',
          message,
          invalidParam,
        });
      }
    }
  } catch {
    // fallback below
  }

  const legacyMatch = LEGACY_BRACKET_PATTERN.exec(raw);
  if (legacyMatch) {
    const code = legacyMatch[1] ?? 'UNKNOWN_ERROR';
    const message = legacyMatch[2] || raw;
    return CertifaceError.fromPayload({
      code,
      message,
    });
  }

  return CertifaceError.fromPayload({
    code: 'UNKNOWN_ERROR',
    message: raw,
  } satisfies LivenessErrorPayload);
}
