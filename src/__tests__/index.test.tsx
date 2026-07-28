import { CertifaceError } from '../errors/CertifaceError';
import { parseNativeError } from '../errors/parseNativeError';

describe('parseNativeError', () => {
  it('parses typed JSON payload with invalidParam', () => {
    const error = parseNativeError(
      JSON.stringify({
        code: 'INVALID_PARAMS',
        message: 'Parâmetros de customização inválidos: color background.',
        invalidParam: 'background',
      })
    );

    expect(error).toBeInstanceOf(CertifaceError);
    expect(error.code).toBe('INVALID_PARAMS');
    expect(error.message).toBe(
      'Parâmetros de customização inválidos: color background.'
    );
    expect(error.invalidParam).toBe('background');
  });

  it('parses legacy bracket format', () => {
    const error = parseNativeError(
      '[LIVENESS_NOT_INITIALIZED]: Não foi possivel inicializar a sessão corretamente.'
    );

    expect(error.code).toBe('LIVENESS_NOT_INITIALIZED');
    expect(error.message).toBe(
      'Não foi possivel inicializar a sessão corretamente.'
    );
    expect(error.invalidParam).toBeUndefined();
  });

  it('falls back to UNKNOWN_ERROR for plain strings', () => {
    const error = parseNativeError('something failed');

    expect(error.code).toBe('UNKNOWN_ERROR');
    expect(error.message).toBe('something failed');
  });
});

describe('CertifaceError', () => {
  it('creates error from payload', () => {
    const error = CertifaceError.fromPayload({
      code: 'INVALID_PARAMS',
      message: 'Parâmetros de customização inválidos: font TITLE_FONT.',
      invalidParam: 'TITLE_FONT',
    });

    expect(error.name).toBe('CertifaceError');
    expect(error.code).toBe('INVALID_PARAMS');
    expect(error.invalidParam).toBe('TITLE_FONT');
  });
});
