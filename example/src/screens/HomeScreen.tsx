import { useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {
  CertifaceError,
  CertifaceSDK,
  Environment,
  LivenessProvider,
  type LivenessResult,
} from '@certiface/sdk';
import { useNavigation } from '@react-navigation/native';
import type { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import type { RootTabParamList } from '../navigation/AppNavigator';
import { useUserStore, type FeatureType } from '../store/userStore';
import { customTheme } from '../constants/customTheme';
import { invalidCustomTheme } from '../constants/invalidCustomTheme';

type HomeNavigationProp = BottomTabNavigationProp<RootTabParamList, 'Home'>;
type JourneyVariant =
  | 'DEFAULT'
  | 'CUSTOM'
  | 'NO_INSTRUCTIONS'
  | 'INVALID_THEME';

const HomeScreen = () => {
  const navigation = useNavigation<HomeNavigationProp>();
  const [loading, setLoading] = useState(false);
  const {
    appKey,
    journeyToken,
    selectedFeature,
    saasProvider,
    environment,
    setSelectedFeature,
    setEnvironment,
    addResult,
    canRunLiveness,
  } = useUserStore();

  const isSaas = selectedFeature === 'SAAS';
  const ready = canRunLiveness();

  const runJourney = async (variant: JourneyVariant) => {
    if (!ready) {
      addResult(
        isSaas
          ? 'ERRO: Journey Token não configurado'
          : 'ERRO: App Key não configurada'
      );
      return;
    }

    if (loading) {
      return;
    }

    const themeEnabled =
      variant === 'CUSTOM' ||
      variant === 'NO_INSTRUCTIONS' ||
      variant === 'INVALID_THEME';
    const hideInstructions = variant === 'NO_INSTRUCTIONS';

    const selectedTheme =
      variant === 'INVALID_THEME'
        ? invalidCustomTheme
        : hideInstructions
          ? {
              ...customTheme,
              instructions: {
                ...customTheme.instructions,
                configuration: {
                  showInstructionScreen: false,
                },
              },
            }
          : customTheme;

    const providerLabel = isSaas ? saasProvider : 'IPROOV';

    try {
      setLoading(true);
      addResult(
        `Iniciando jornada (${providerLabel} | ${environment} | ${
          isSaas ? 'SaaS' : 'AppKey'
        } | tema ${themeEnabled ? 'ON' : 'OFF'})`
      );

      const result: LivenessResult = isSaas
        ? await CertifaceSDK.startSaasJourney(
            journeyToken,
            environment,
            themeEnabled,
            themeEnabled ? selectedTheme : undefined
          )
        : await CertifaceSDK.startJourney(
            appKey,
            environment,
            LivenessProvider.IPROOV,
            themeEnabled,
            themeEnabled ? selectedTheme : undefined
          );

      addResult(
        `✅ Sucesso: valid=${result?.valid} codID=${result?.codID} protocol=${result?.protocol}`
      );
      navigation.navigate('Results');
    } catch (error) {
      if (error instanceof CertifaceError) {
        const invalidParam = error.invalidParam
          ? ` invalidParam=${error.invalidParam}`
          : '';
        addResult(
          `ERRO: code=${error.code} message=${error.message}${invalidParam}`
        );
      } else {
        addResult(`ERRO: ${error}`);
      }
      navigation.navigate('Results');
    } finally {
      setLoading(false);
    }
  };

  const renderFeatureOption = (feature: FeatureType, label: string) => (
    <Pressable
      key={feature}
      style={[
        styles.segmentOption,
        selectedFeature === feature && styles.segmentOptionActive,
      ]}
      onPress={() => setSelectedFeature(feature)}
    >
      <Text
        style={[
          styles.segmentOptionText,
          selectedFeature === feature && styles.segmentOptionTextActive,
        ]}
      >
        {label}
      </Text>
    </Pressable>
  );

  return (
    <SafeAreaView style={styles.safeArea}>
      <ScrollView
        style={styles.scrollView}
        contentContainerStyle={styles.scrollContent}
      >
        <View style={styles.header}>
          <Text style={styles.title}>Certiface SDK</Text>
          <Text style={styles.subtitle}>
            {isSaas ? 'Teste SaaS (FaceTec / Fortface)' : 'Teste iProov'}
          </Text>
        </View>

        <View style={styles.card}>
          <View style={styles.statusContainer}>
            <Text style={styles.statusLabel}>
              {isSaas ? 'Journey Token' : 'App Key'}
            </Text>
            <Text
              style={[
                styles.statusText,
                ready ? styles.statusReady : styles.statusNotReady,
              ]}
            >
              {ready ? 'Pronto para uso' : 'Não configurado'}
            </Text>
          </View>

          <Text style={styles.segmentTitle}>Produto</Text>
          <View style={styles.segment}>
            {renderFeatureOption('IPROOV', 'iProov')}
            {renderFeatureOption('SAAS', 'SaaS')}
          </View>

          <Text style={styles.segmentTitle}>Ambiente</Text>
          <View style={styles.segment}>
            <Pressable
              style={[
                styles.segmentOption,
                environment === Environment.HML && styles.segmentOptionActive,
              ]}
              onPress={() => setEnvironment(Environment.HML)}
            >
              <Text
                style={[
                  styles.segmentOptionText,
                  environment === Environment.HML &&
                    styles.segmentOptionTextActive,
                ]}
              >
                HML
              </Text>
            </Pressable>
            <Pressable
              style={[
                styles.segmentOption,
                environment === Environment.PRD && styles.segmentOptionActive,
              ]}
              onPress={() => setEnvironment(Environment.PRD)}
            >
              <Text
                style={[
                  styles.segmentOptionText,
                  environment === Environment.PRD &&
                    styles.segmentOptionTextActive,
                ]}
              >
                PRD
              </Text>
            </Pressable>
          </View>

          <Text style={styles.providerHint}>
            Fluxo: {isSaas ? `SaaS (${saasProvider})` : 'IPROOV'}
          </Text>
        </View>

        <Text style={styles.sectionTitle}>Ações</Text>

        <View style={styles.actionsList}>
          <TouchableOpacity
            style={[
              styles.actionCard,
              styles.primaryAction,
              (!ready || loading) && styles.actionDisabled,
            ]}
            onPress={() => runJourney('DEFAULT')}
            disabled={!ready || loading}
          >
            <Text style={styles.primaryActionTitle}>Default</Text>
            <Text style={styles.primaryActionDescription}>Fluxo normal</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[
              styles.actionCard,
              styles.secondaryAction,
              (!ready || loading) && styles.actionDisabled,
            ]}
            onPress={() => runJourney('CUSTOM')}
            disabled={!ready || loading}
          >
            <Text style={styles.secondaryActionTitle}>Custom</Text>
            <Text style={styles.secondaryActionDescription}>
              Tema customizado
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[
              styles.actionCard,
              styles.secondaryAction,
              (!ready || loading) && styles.actionDisabled,
            ]}
            onPress={() => runJourney('NO_INSTRUCTIONS')}
            disabled={!ready || loading}
          >
            <Text style={styles.secondaryActionTitle}>Sem instruções</Text>
            <Text style={styles.secondaryActionDescription}>
              Com tema customizado
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[
              styles.actionCard,
              styles.dangerAction,
              (!ready || loading) && styles.actionDisabled,
            ]}
            onPress={() => runJourney('INVALID_THEME')}
            disabled={!ready || loading}
          >
            <Text style={styles.dangerActionTitle}>Tema inválido</Text>
            <Text style={styles.dangerActionDescription}>
              Testa INVALID_PARAMS
            </Text>
          </TouchableOpacity>
        </View>

        {loading ? (
          <View style={styles.loadingRow}>
            <ActivityIndicator size="small" color="#2563EB" />
            <Text style={styles.loadingText}>Executando jornada...</Text>
          </View>
        ) : null}
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#F1F5F9',
  },
  scrollView: {
    flex: 1,
  },
  scrollContent: {
    padding: 16,
    paddingBottom: 110,
  },
  header: {
    backgroundColor: '#1E3A8A',
    borderRadius: 18,
    paddingHorizontal: 18,
    paddingVertical: 16,
    marginBottom: 20,
    shadowColor: '#1E293B',
    shadowOpacity: 0.14,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 5,
  },
  title: {
    fontSize: 24,
    fontWeight: '800',
    color: '#FFFFFF',
    textAlign: 'left',
  },
  subtitle: {
    fontSize: 13,
    marginTop: 4,
    color: '#E2E8F0',
    textAlign: 'left',
  },
  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 16,
    marginBottom: 12,
    shadowColor: '#1E293B',
    shadowOpacity: 0.1,
    shadowRadius: 10,
    shadowOffset: { width: 0, height: 4 },
    elevation: 5,
  },
  statusContainer: {
    borderRadius: 10,
    padding: 12,
    marginBottom: 14,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#F8FAFC',
  },
  statusLabel: {
    fontSize: 15,
    fontWeight: '600',
    color: '#111827',
  },
  statusText: {
    fontSize: 13,
    fontWeight: '600',
  },
  statusReady: {
    color: '#16A34A',
  },
  statusNotReady: {
    color: '#EF4444',
  },
  segmentTitle: {
    fontSize: 14,
    fontWeight: '700',
    color: '#1E293B',
    marginBottom: 8,
    marginTop: 6,
  },
  segment: {
    flexDirection: 'row',
    backgroundColor: '#E2E8F0',
    borderRadius: 12,
    padding: 4,
    marginBottom: 10,
  },
  segmentOption: {
    flex: 1,
    borderRadius: 12,
    paddingVertical: 10,
    alignItems: 'center',
  },
  segmentOptionActive: {
    backgroundColor: '#1D4ED8',
  },
  segmentOptionText: {
    fontSize: 13,
    fontWeight: '700',
    color: '#475569',
  },
  segmentOptionTextActive: {
    color: '#FFFFFF',
  },
  providerHint: {
    fontSize: 12,
    color: '#475569',
    marginTop: 4,
    fontWeight: '600',
  },
  sectionTitle: {
    marginTop: 8,
    marginBottom: 12,
    fontSize: 17,
    fontWeight: '800',
    color: '#0F172A',
  },
  actionsList: {
    gap: 10,
  },
  actionCard: {
    width: '100%',
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    padding: 14,
    minHeight: 94,
    justifyContent: 'space-between',
    shadowColor: '#1E293B',
    shadowOpacity: 0.08,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 4,
  },
  primaryAction: {
    backgroundColor: '#DBEAFE',
  },
  secondaryAction: {
    backgroundColor: '#FFF7ED',
  },
  dangerAction: {
    backgroundColor: '#FEE2E2',
  },
  actionDisabled: {
    opacity: 0.5,
  },
  primaryActionTitle: {
    color: '#1E3A8A',
    fontSize: 15,
    fontWeight: '700',
  },
  primaryActionDescription: {
    color: '#1D4ED8',
    fontSize: 13,
  },
  secondaryActionTitle: {
    color: '#9A3412',
    fontSize: 15,
    fontWeight: '700',
  },
  secondaryActionDescription: {
    color: '#C2410C',
    fontSize: 13,
  },
  dangerActionTitle: {
    color: '#991B1B',
    fontSize: 15,
    fontWeight: '700',
  },
  dangerActionDescription: {
    color: '#DC2626',
    fontSize: 13,
  },
  loadingRow: {
    marginTop: 18,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
  },
  loadingText: {
    color: '#1D4ED8',
    fontSize: 14,
    fontWeight: '600',
  },
});

export default HomeScreen;
