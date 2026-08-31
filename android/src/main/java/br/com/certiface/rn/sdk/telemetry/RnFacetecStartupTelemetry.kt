package br.com.certiface.rn.sdk.telemetry

import br.com.certiface.core.telemetry.FacetecStartupTelemetry

object RnFacetecStartupTelemetry {
  fun ensureSession(origin: String = "rn_saas") {
    if (!FacetecStartupTelemetry.isActive()) {
      FacetecStartupTelemetry.beginSession(origin)
    }
  }

  fun mark(step: String, durationMs: Long? = null, extra: String? = null) {
    FacetecStartupTelemetry.mark(step, durationMs, extra)
  }

  fun <T> measure(step: String, block: () -> T): T =
    FacetecStartupTelemetry.measure(step, block)

  fun abort(reason: String) {
    FacetecStartupTelemetry.abort(reason)
  }
}
