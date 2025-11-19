import { useState } from 'react';
import {
  Text,
  View,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  SafeAreaView,
  KeyboardAvoidingView,
  Platform,
  Switch,
} from 'react-native';
import {
  checkCameraPermission,
  requestCameraPermission,
  startJourney,
  Environment,
  LivenessProvider,
  type OitiTheme,
  type LivenessResult,
} from '@oiti/rn-sdk';
import { useUserStore } from '../store/userStore';

const HomeScreen = () => {
  const [results, setResults] = useState<string[]>([]);
  const [isCustomEnabled, setIsCustomEnabled] = useState(false);
  const [environment, setEnvironment] = useState<Environment>(Environment.HML);
  const [provider, setProvider] = useState<LivenessProvider>(
    LivenessProvider.FACETEC
  );
  const { appKey, setAppKey, setLivenessProvider } = useUserStore();

  const customTheme: OitiTheme = {
    facetec: {
      colors: {
        readyScreenHeader: '#FFFFFF',
        readyScreenSubtext: '#CCCCCC',
        readyScreenOvalFill: '#FF6B35',
        readyScreenTextBackground: '#444444',
        resultScreenForeground: '#FF6B35',
        resultScreenBackground: '#F0F8FF',
        ovalStroke: '#FF6B35',
        ovalProgressFirst: '#FF6B35',
        ovalProgressSecond: '#FFD700',
        overlayBackground: '#80000000',
        frameBorder: '#FF6B35',
        frameBackground: '#1A1A1A',
        feedbackBarBackground: '#FFF8DC',
        feedbackMessage: '#333333',
        guidanceBackground: '#2E2E2E',
        guidanceForeground: '#FFFFFF',
        guidanceButtonTextNormal: '#FFFFFF',
        guidanceButtonTextHighlight: '#FFFFFF',
        guidanceButtonTextDisabled: '#AAAAAA',
        guidanceButtonBackgroundNormal: '#FF6B35',
        guidanceButtonBackgroundHighlight: '#FF6B35',
        guidanceButtonBackgroundDisabled: '#666666',
        guidanceButtonBorder: '#FF6B35',
      },
      texts: {
        readyHeader1: 'Prepare-se',
        readyHeader2: 'para verificação',
        readyMessage1: 'Posicione seu rosto',
        readyMessage2: 'dentro do círculo',
        readyButton: 'Iniciar',
        retryHeader: 'Vamos tentar novamente',
        retrySubheader: 'Ajustes necessários',
        retryButton: 'Tentar Novamente',
        resultSuccessMessage: 'Verificação concluída!',
        feedbackCenterFace: 'Centralize seu rosto',
        feedbackHoldSteady: 'Mantenha-se parado',
        feedbackMovePhoneCloser: 'Aproxime o dispositivo',
        feedbackMovePhoneAway: 'Afaste o dispositivo',
      },
      assets: {
        overlayBrandImage: 'shell',
        cancelButtonIcon: 'shell',
        resultScreenCustomActivityIndicatorImage: 'shell',
      },
      fonts: {
        readyScreenHeader: 'sixty',
        readyScreenSubtext: 'sixty',
        resultScreenMessage: 'sixty',
        retryScreenHeader: 'sixty',
        retryScreenSubtext: 'sixty',
        feedbackMessage: 'sixty',
        guidanceHeader: 'sixty',
        guidanceSubtext: 'sixty',
        guidanceButton: 'sixty',
      },
    },
    iproov: {
      colors: {
        title: '#FFFFFF',
        titleBackground: '#2E2E2E',
        promptText: '#FFFFFF',
        promptBackground: '#1A1A1A',
        background: '#FF6B35',
        ovalReady: '#FF6B35',
        ovalNotReady: '#FF3030',
        ovalCapturing: '#FFFFFF',
        ovalCompleted: '#FF6B35',
      },
      texts: {
        title: 'Verificação Biométrica',
      },
      assets: {
        closeButtonIcon: 'shell',
        logoImage: 'shell',
      },
      fonts: {
        instructionsTitleFont: 'sixty',
        instructionsCaptionFont: 'sixty',
        instructionsDocumentTypesInstructionsFont: 'sixty',
        instructionsDocumentTipsInstructionsFont: 'sixty',
        instructionsButtonFont: 'sixty',
        permissionTitleFont: 'sixty',
        permissionCaptionFont: 'sixty',
        permissionButtonFont: 'sixty',
        resultMessageFont: 'sixty',
        resultRetryButtonFont: 'sixty',
      },
    },
    instructions: {
      colors: {
        statusBar: '#2E2E2E',
        background: '#2E2E2E',
        backButtonIcon: '#2E2E2E',
        backButtonBackground: '#2E2E2E',
        backButtonBorder: '#2E2E2E',
        bottomSheet: '#1A1A1A',
        title: '#FFFFFF',
        caption: '#CCCCCC',
        firstInstructionTitle: '#FFFFFF',
        secondInstructionTitle: '#FFFFFF',
        continueButtonText: '#FFFFFF',
        continueButtonBackground: '#FF6B35',
        continueButtonBorder: '#FF6B35',
      },
      texts: {
        title: 'Verificação de Identidade',
        caption: 'Siga as instruções para completar o processo',
        firstInstruction: 'Mantenha o documento bem iluminado',
        secondInstruction: 'Use um documento oficial com foto',
        continueButton: 'Continuar',
      },
      assets: {
        backButtonIcon: 'shell',
        contextImage: 'shell',
        firstInstructionIcon: 'shell',
        secondInstructionIcon: 'shell',
      },
      fonts: {
        title: 'sixty',
        caption: 'sixty',
        firstInstructionTitle: 'sixty',
        secondInstructionTitle: 'sixty',
        continueButton: 'sixty',
      },
    },
    permission: {
      colors: {
        statusBar: '#2E2E2E',
        background: '#2E2E2E',
        backButtonIcon: '#2E2E2E',
        backButtonBackground: '#2E2E2E',
        backButtonBorder: '#2E2E2E',
        cameraImage: '#FFFFFF',
        title: '#FFFFFF',
        caption: '#FFFFFF',
        checkPermissionButtonText: '#FFFFFF',
        checkPermissionButtonBackground: '#FF6B35',
        checkPermissionButtonBorder: '#FF6B35',
        bottomSheet: '#FF6B35',
        bottomSheetTitle: '#FF6B35',
        bottomSheetCaption: '#FF6B35',
        openSettingsButtonText: '#FF6B35',
        openSettingsButtonBackground: '#FF6B35',
        openSettingsButtonBorder: '#FF6B35',
        closeButtonText: '#FF6B35',
        closeButtonBackground: '#FF6B35',
        closeButtonBorder: '#FF6B35',
      },
      texts: {
        title: 'Permissões Necessárias',
        caption: 'Permissões Necessárias',
        checkPermissionButton: 'Permitir Acesso',
        bottomSheetTitle: 'Permitir Acesso',
        bottomSheetCaption: 'Permitir Acesso',
        openSettingsButton: 'Permitir Acesso',
        closeButton: 'Permitir Acesso',
      },
      assets: {
        backButtonIcon: 'shell',
        cameraImage: 'shell',
      },
      fonts: {
        title: 'sixty',
        caption: 'sixty',
        checkPermissionButton: 'sixty',
        bottomSheetTitle: 'sixty',
        bottomSheetCaption: 'sixty',
        opentSettingsButton: 'sixty',
        closeButton: 'sixty',
      },
    },
    processing: {
      colors: {
        statusBar: '#1A1A1A',
        background: '#1A1A1A',
        loading: '#FFFFFF',
      },
    },
    result: {
      colors: {
        successStatusBar: '#E8F5E8',
        successBackground: '#E8F5E8',
        successText: '#2E7D32',
        errorStatusBar: '#FFEBEE',
        errorBackground: '#FFEBEE',
        errorText: '#C62828',
        retryBackground: '#C62828',
        retryText: '#FFEBEE',
        retryButtonText: '#FF6B35',
        retryButtonBackground: '#FFFFFF',
        retryButtonBorder: '#FFFFFF',
      },
      texts: {
        success: 'Verificação concluída com sucesso!',
        error: 'Houve um erro na verificação. Tente novamente.',
        retryButton: 'Tentar Novamente',
      },
      assets: {
        successImage: 'shell',
        errorImage: 'shell',
        retryImage: 'shell',
      },
      fonts: {
        text: 'sixty',
        retryButton: 'sixty',
      },
    },
  };

  const addResult = (message: string) => {
    setResults((prev) => [
      ...prev,
      `${new Date().toLocaleTimeString()}: ${message}`,
    ]);
  };

  const handleCheckPermission = async () => {
    try {
      const hasPermission = await checkCameraPermission();
      addResult(
        `Camera permission check: ${hasPermission ? 'Granted' : 'Denied'}`
      );
    } catch (error) {
      addResult(`Camera permission check error: ${error}`);
    }
  };

  const handleRequestPermission = async () => {
    try {
      const granted = await requestCameraPermission();
      addResult(`Camera permission request: ${granted ? 'Granted' : 'Denied'}`);
    } catch (error) {
      addResult(`Camera permission request error: ${error}`);
    }
  };

  const handleStartJourney = async () => {
    if (!appKey) {
      addResult('Error: App Key not set. Go to Session to generate one.');
      return;
    }

    try {
      addResult(
        `Starting journey with provider: ${provider}, environment: ${environment}, custom theme: ${isCustomEnabled ? 'ENABLED' : 'DISABLED'}`
      );
      const result: LivenessResult = await startJourney(
        appKey,
        environment,
        provider,
        isCustomEnabled,
        isCustomEnabled ? customTheme : undefined
      );

      const { valid, codID, protocol } = result;
      addResult(
        `✅ Liveness Success - Valid: ${valid}, CodID: ${codID}, Protocol: ${protocol}`
      );
    } catch (error) {
      addResult(`Start Journey error: ${error}`);
    }
  };

  const clearResults = () => {
    setResults([]);
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <KeyboardAvoidingView
        style={styles.keyboardAvoidingView}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 0 : 20}
      >
        <ScrollView
          style={styles.scrollView}
          contentContainerStyle={styles.scrollContent}
          keyboardShouldPersistTaps="handled"
          showsVerticalScrollIndicator={false}
        >
          <Text style={styles.subtitle}>SDK Test Functions</Text>

          <View style={styles.statusContainer}>
            <Text style={styles.statusLabel}>App Key Status:</Text>
            <Text
              style={[
                styles.statusText,
                appKey ? styles.statusReady : styles.statusNotReady,
              ]}
            >
              {appKey ? 'Ready' : 'Not Set - Go to Session'}
            </Text>
          </View>

          <View style={styles.switchContainer}>
            <Text style={styles.switchLabel}>Provider:</Text>
            <Switch
              value={provider === LivenessProvider.IPROOV}
              onValueChange={(value) => {
                const newProvider = value
                  ? LivenessProvider.IPROOV
                  : LivenessProvider.FACETEC;
                setProvider(newProvider);
                setLivenessProvider(newProvider);
                setAppKey('');
              }}

              trackColor={{ false: '#767577', true: '#4A90E2' }}
              thumbColor={
                provider === LivenessProvider.IPROOV ? '#FFFFFF' : '#f4f3f4'
              }
            />
            <Text style={styles.switchStatus}>{provider}</Text>
          </View>

          <View style={styles.switchContainer}>
            <Text style={styles.switchLabel}>Environment:</Text>
            <Switch
              value={environment === Environment.PRD}
              onValueChange={(value) =>
                setEnvironment(value ? Environment.PRD : Environment.HML)
              }
              trackColor={{ false: '#767577', true: '#0F9D58' }}
              thumbColor={
                environment === Environment.PRD ? '#FFFFFF' : '#f4f3f4'
              }
            />
            <Text style={styles.switchStatus}>{environment}</Text>
          </View>

          <View style={styles.switchContainer}>
            <Text style={styles.switchLabel}>Custom Theme:</Text>
            <Switch
              value={isCustomEnabled}
              onValueChange={setIsCustomEnabled}
              trackColor={{ false: '#767577', true: '#FF6B35' }}
              thumbColor={isCustomEnabled ? '#FFFFFF' : '#f4f3f4'}
            />
            <Text style={styles.switchStatus}>
              {isCustomEnabled ? 'ON' : 'OFF'}
            </Text>
          </View>

          <View style={styles.buttonContainer}>
            <TouchableOpacity
              style={styles.button}
              onPress={handleCheckPermission}
            >
              <Text style={styles.buttonText}>Check Camera Permission</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.button}
              onPress={handleRequestPermission}
            >
              <Text style={styles.buttonText}>Request Camera Permission</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.button, !appKey && styles.buttonDisabled]}
              onPress={handleStartJourney}
              disabled={!appKey}
            >
              <Text style={styles.buttonText}>Start Journey</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.clearButton} onPress={clearResults}>
              <Text style={styles.buttonText}>Clear Results</Text>
            </TouchableOpacity>
          </View>

          <View style={styles.resultsContainer}>
            <Text style={styles.resultsTitle}>Results:</Text>
            <ScrollView style={styles.resultsScroll}>
              {results.length === 0 ? (
                <Text style={styles.noResults}>
                  Sem resultados, tente utilizar os botões!
                </Text>
              ) : (
                results.map((result, index) => (
                  <Text key={index} style={styles.resultText}>
                    {result}
                  </Text>
                ))
              )}
            </ScrollView>
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  keyboardAvoidingView: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  scrollContent: {
    padding: 20,
    paddingBottom: 40,
  },
  subtitle: {
    fontSize: 18,
    fontWeight: '600',
    textAlign: 'center',
    marginBottom: 20,
    color: '#333',
  },
  statusContainer: {
    backgroundColor: 'white',
    borderRadius: 8,
    padding: 15,
    marginBottom: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  statusLabel: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
  },
  statusText: {
    fontSize: 14,
    fontWeight: '600',
  },
  statusReady: {
    color: '#0F9D58',
  },
  statusNotReady: {
    color: '#FF3B30',
  },
  switchContainer: {
    backgroundColor: 'white',
    borderRadius: 8,
    padding: 15,
    marginBottom: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  switchLabel: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
  },
  switchStatus: {
    fontSize: 14,
    fontWeight: '600',
    color: '#333',
    marginLeft: 10,
  },
  buttonContainer: {
    marginBottom: 20,
  },
  button: {
    backgroundColor: '#007AFF',
    padding: 15,
    borderRadius: 8,
    marginBottom: 10,
    alignItems: 'center',
  },
  buttonDisabled: {
    backgroundColor: '#cccccc',
  },
  clearButton: {
    backgroundColor: '#FF3B30',
    padding: 15,
    borderRadius: 8,
    marginBottom: 10,
    alignItems: 'center',
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
  resultsContainer: {
    minHeight: 200,
    backgroundColor: 'white',
    borderRadius: 8,
    padding: 15,
    marginBottom: 20,
  },
  resultsTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#333',
  },
  resultsScroll: {
    flex: 1,
  },
  resultText: {
    fontSize: 14,
    color: '#666',
    marginBottom: 5,
    fontFamily: 'monospace',
  },
  noResults: {
    fontSize: 14,
    color: '#999',
    fontStyle: 'italic',
    textAlign: 'center',
    marginTop: 20,
  },
});

export default HomeScreen;
