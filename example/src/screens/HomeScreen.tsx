import { useState } from 'react';
import {
  ActivityIndicator,
  Alert,
  Pressable,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { CertifaceSDK, Environment, type LivenessResult } from '@certiface/sdk';
import { useNavigation } from '@react-navigation/native';
import type { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import type { RootTabParamList } from '../navigation/AppNavigator';
import { useUserStore } from '../store/userStore';
import { customTheme } from '../constants/customTheme';

type HomeNavigationProp = BottomTabNavigationProp<RootTabParamList, 'Home'>;
type JourneyVariant = 'DEFAULT' | 'CUSTOM' | 'NO_INSTRUCTIONS';

const HomeScreen = () => {
  const navigation = useNavigation<HomeNavigationProp>();
  const [loading, setLoading] = useState(false);
  const {
    appKey,
    selectedFeature,
    livenessProvider,
    environment,
    setSelectedFeature,
    setEnvironment,
    addResult,
  } = useUserStore();

  const runJourney = async (variant: JourneyVariant) => {
    if (!appKey) {
      Alert.alert(
        'App Key ausente',
        'Informe ou gere a App Key na aba Credencial.'
      );
      addResult('ERRO: App Key não configurada');
      return;
    }

    if (loading) {
      return;
    }

    const themeEnabled = variant === 'CUSTOM' || variant === 'NO_INSTRUCTIONS';
    const hideInstructions = variant === 'NO_INSTRUCTIONS';

    const selectedTheme = hideInstructions
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

    try {
      setLoading(true);
      addResult(
        `Iniciando jornada (${selectedFeature} | ${environment} | tema ${themeEnabled ? 'ON' : 'OFF'})`
      );
      const result: LivenessResult = await CertifaceSDK.startJourney(
        appKey,
        environment,
        livenessProvider,
        themeEnabled,
        themeEnabled ? selectedTheme : undefined
      );

      const { valid, codID, protocol } = result;
      addResult(
        `✅ Sucesso: valid=${valid} codID=${codID} protocol=${protocol}`
      );
      navigation.navigate('Results');
    } catch (error) {
      addResult(`ERRO: ${error}`);
      navigation.navigate('Results');
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <ScrollView
        style={styles.scrollView}
        contentContainerStyle={styles.scrollContent}
      >
        <View style={styles.header}>
          <Text style={styles.title}>Certiface SDK</Text>
          <Text style={styles.subtitle}>Teste de jornada</Text>
        </View>

        <View style={styles.card}>
          <View style={styles.statusContainer}>
            <Text style={styles.statusLabel}>App Key</Text>
            <Text
              style={[
                styles.statusText,
                appKey ? styles.statusReady : styles.statusNotReady,
              ]}
            >
              {appKey ? 'Pronta para uso' : 'Não configurada'}
            </Text>
          </View>

          <Text style={styles.segmentTitle}>Feature</Text>
          <View style={styles.segment}>
            <Pressable
              style={[
                styles.segmentOption,
                selectedFeature === 'FACETEC' && styles.segmentOptionActive,
              ]}
              onPress={() => setSelectedFeature('FACETEC')}
            >
              <Text
                style={[
                  styles.segmentOptionText,
                  selectedFeature === 'FACETEC' &&
                    styles.segmentOptionTextActive,
                ]}
              >
                Facetec
              </Text>
            </Pressable>
            <Pressable
              style={[
                styles.segmentOption,
                selectedFeature === 'IPROOV' && styles.segmentOptionActive,
              ]}
              onPress={() => setSelectedFeature('IPROOV')}
            >
              <Text
                style={[
                  styles.segmentOptionText,
                  selectedFeature === 'IPROOV' &&
                    styles.segmentOptionTextActive,
                ]}
              >
                iProov
              </Text>
            </Pressable>
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
        </View>

        <Text style={styles.sectionTitle}>Ações</Text>

        <View style={styles.actionsList}>
          <TouchableOpacity
            style={[
              styles.actionCard,
              styles.primaryAction,
              (!appKey || loading) && styles.actionDisabled,
            ]}
            onPress={() => runJourney('DEFAULT')}
            disabled={!appKey || loading}
          >
            <Text style={styles.primaryActionTitle}>Default</Text>
            <Text style={styles.primaryActionDescription}>Fluxo normal</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[
              styles.actionCard,
              styles.secondaryAction,
              (!appKey || loading) && styles.actionDisabled,
            ]}
            onPress={() => runJourney('CUSTOM')}
            disabled={!appKey || loading}
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
              (!appKey || loading) && styles.actionDisabled,
            ]}
            onPress={() => runJourney('NO_INSTRUCTIONS')}
            disabled={!appKey || loading}
          >
            <Text style={styles.secondaryActionTitle}>Sem instruções</Text>
            <Text style={styles.secondaryActionDescription}>
              Com tema customizado
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
  actionDisabled: {
    opacity: 0.5,
  },
  actionTitle: {
    color: '#1F2937',
    fontSize: 15,
    fontWeight: '600',
  },
  actionDescription: {
    color: '#6B7280',
    fontSize: 13,
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
