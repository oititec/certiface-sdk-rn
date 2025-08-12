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
  LivenessProvider,
  type OitiTheme,
} from '@oiti/rn-sdk';
import { useUserStore } from '../store/userStore';

const HomeScreen = () => {
  const [results, setResults] = useState<string[]>([]);
  const [isCustomEnabled, setIsCustomEnabled] = useState(false);
  const { appKey } = useUserStore();

  const customTheme: OitiTheme = {
    provider: LivenessProvider.FACETEC,
    facetec: {
      colors: {
        guidanceBackgroundColors: '#2E2E2E',
        guidanceForegroundColor: '#FFFFFF',
        guidanceReadyScreenHeaderTextColor: '#FFFFFF',
        guidanceReadyScreenSubtextTextColor: '#CCCCCC',
        guidanceButtonBackgroundHighlightColor: '#FF6B35',
        guidanceButtonTextHighlightColor: '#FFFFFF',
        guidanceButtonBorderColor: '#FF6B35',
        guidanceReadyScreenOvalFillColor: '#FF6B35',
        resultScreenForegroundColor: '#FF6B35',
        resultScreenBackgroundColors: '#F0F8FF',
        ovalCustomizationStrokeColor: '#FF6B35',
        ovalCustomizationProgressColor1: '#FF6B35',
        ovalCustomizationProgressColor2: '#FFD700',
        frameBorderColor: '#FF6B35',
        frameBackgroundColor: '#1A1A1A',
        overlayBackgroundColor: '#80000000',
        feedbackBackgroundColors: '#FFF8DC',
        feedbackTextColor: '#333333',
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
      fonts: {
        instructionsTitleFont: 'sixty',
        instructionsCaptionFont: 'sixty',
        guidanceCustomizationHeaderFont: 'sixty',
        guidanceCustomizationSubtextFont: 'sixty',
        permissionTitleFont: 'sixty',
        resultScreenCustomizationMessageFont: 'sixty',
        feedbackCustomizationTextFont: 'sixty',
        instructionsDocumentTypesInstructionsFont: 'sixty',
        instructionsDocumentTipsInstructionsFont: 'sixty',
        instructionsButtonFont: 'sixty',
        permissionCaptionFont: 'sixty',
        permissionButtonFont: 'sixty',
        guidanceCustomizationReadyScreenHeaderFont: 'sixty',
        guidanceCustomizationReadyScreenSubtextFont: 'sixty',
        guidanceCustomizationRetryScreenHeaderFont: 'sixty',
        guidanceCustomizationRetryScreenSubtextFont: 'sixty',
        guidanceCustomizationButtonFont: 'sixty',
      },
    },
    iproov: {
      colors: {
        titleColor: '#FFFFFF',
        headerBackgroundColor: '#2E2E2E',
        promptTextColor: '#FFFFFF',
        promptBackgroundColor: '#1A1A1A',
        surroundColor: '#FF6B35',
        ovalReadyColor: '#FF6B35',
        ovalNotReadyColor: '#FF3030',
        ovalStrokeColor: '#FFFFFF',
        ovalCompletedColor: '#FF6B35',
      },
      texts: {
        title: 'Verificação Biométrica',
        instructionsTitleText: 'Verificação Facial',
        instructionsCaptionText:
          'Siga as instruções para completar a verificação',
        continueButtonText: 'Começar',
        permissionTitle: 'Permissão de Câmera',
        checkPermissionButtonText: 'Permitir',
        successText: 'Verificação realizada com sucesso!',
        errorText: 'Falha na verificação. Tente novamente.',
        retryButtonText: 'Tentar Novamente',
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
        titleColor: '#FFFFFF',
        captionColor: '#CCCCCC',
        backgroundColor: '#2E2E2E',
        statusBarColor: '#2E2E2E',
        bottomSheetColor: '#1A1A1A',
        continueButtonColor: '#FF6B35',
        continueButtonTextColor: '#FFFFFF',
      },
      texts: {
        titleText: 'Verificação de Identidade',
        captionText: 'Siga as instruções para completar o processo',
        documentTipsInstructionText: 'Mantenha o documento bem iluminado',
        documentTypesInstructionText: 'Use um documento oficial com foto',
        continueButtonText: 'Continuar',
      },
    },
    permission: {
      colors: {
        titleColor: '#FFFFFF',
        backgroundColor: '#2E2E2E',
        statusBarColor: '#2E2E2E',
        checkPermissionButtonColor: '#FF6B35',
        checkPermissionButtonTextColor: '#FFFFFF',
      },
      texts: {
        title: 'Permissões Necessárias',
        checkPermissionButtonText: 'Permitir Acesso',
      },
    },
    processing: {
      colors: {
        backgroundColor: '#1A1A1A',
        loadingDialogColor: '#FFFFFF',
        statusBarColor: '#1A1A1A',
      },
    },
    result: {
      colors: {
        successBackgroundColor: '#E8F5E8',
        successTextColor: '#2E7D32',
        errorBackgroundColor: '#FFEBEE',
        errorTextColor: '#C62828',
        statusBarSuccessColor: '#E8F5E8',
        statusBarErrorColor: '#FFEBEE',
        retryButtonColor: '#FF6B35',
        retryButtonTextColor: '#FFFFFF',
      },
      texts: {
        successText: 'Verificação concluída com sucesso!',
        errorText: 'Houve um erro na verificação. Tente novamente.',
        retryButtonText: 'Tentar Novamente',
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
        `Starting journey with custom theme: ${isCustomEnabled ? 'ENABLED' : 'DISABLED'}`
      );
      const result = await startJourney(
        appKey,
        isCustomEnabled,
        isCustomEnabled ? customTheme : undefined
      );
      addResult(`Start Journey result: ${result}`);
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
