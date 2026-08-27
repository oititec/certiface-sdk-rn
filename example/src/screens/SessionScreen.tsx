import { useState } from 'react';
import {
  ActivityIndicator,
  Alert,
  KeyboardAvoidingView,
  Pressable,
  Platform,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  View,
  Text,
  TextInput,
  TouchableOpacity,
} from 'react-native';
import Clipboard from '@react-native-clipboard/clipboard';
import Ionicons from 'react-native-vector-icons/Ionicons';
import { Environment, type SaasProvider } from '@certiface/sdk';
import { useNavigation } from '@react-navigation/native';
import type { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import type { RootTabParamList } from '../navigation/AppNavigator';
import { useUserStore, type FeatureType } from '../store/userStore';
import { maskBirthDate } from '../utils/maskBirthDate';
import { maskCpf } from '../utils/maskCpf';

type SessionNavigationProp = BottomTabNavigationProp<
  RootTabParamList,
  'Credential'
>;

const SessionScreen = () => {
  const navigation = useNavigation<SessionNavigationProp>();
  const {
    userData,
    saasOperator,
    appKey,
    journeyToken,
    setUserData,
    setSaasOperator,
    setAppKey,
    setJourneyToken,
    generateAppKey,
    generateJourneyToken,
    clearSaasSession,
    selectedFeature,
    saasProvider,
    environment,
    setSelectedFeature,
    setSaasProvider,
    setEnvironment,
    addResult,
  } = useUserStore();
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const isSaas = selectedFeature === 'SAAS';

  const handleCopy = (value: string, label: string) => {
    if (!value) {
      return;
    }
    Clipboard.setString(value);
    Alert.alert('Copiado', `${label} copiado para a área de transferência.`);
  };

  const handleGenerate = async () => {
    try {
      setLoading(true);
      if (isSaas) {
        const token = await generateJourneyToken();
        addResult(`Token SAAS (${saasProvider}) gerado: ${token}`);
      } else {
        const generated = await generateAppKey();
        addResult(`AppKey gerada: ${generated}`);
      }
      navigation.navigate('Home');
    } catch (error) {
      const message = `Falha ao gerar ${isSaas ? 'token SAAS' : 'App Key'}: ${error}`;
      Alert.alert('Erro', message);
      addResult(`ERRO: ${message}`);
      navigation.navigate('Results');
    } finally {
      setLoading(false);
    }
  };

  const handleClearSaas = async () => {
    try {
      setLoading(true);
      await clearSaasSession();
      addResult('Token SAAS removido');
    } catch (error) {
      addResult(`AVISO ao limpar sessão SAAS: ${error}`);
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

  const renderSaasProviderOption = (provider: SaasProvider, label: string) => (
    <Pressable
      key={provider}
      style={[
        styles.segmentOption,
        saasProvider === provider && styles.segmentOptionActive,
      ]}
      onPress={() => setSaasProvider(provider)}
    >
      <Text
        style={[
          styles.segmentOptionText,
          saasProvider === provider && styles.segmentOptionTextActive,
        ]}
      >
        {label}
      </Text>
    </Pressable>
  );

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
          <View style={styles.header}>
            <Text style={styles.title}>
              {isSaas ? 'Sessão SAAS' : 'Credencial'}
            </Text>
            <Text style={styles.subtitle}>
              {isSaas
                ? 'Gere journeyToken (FaceTec / Fortface)'
                : 'Gere AppKey para iProov'}
            </Text>
          </View>

          <View style={styles.section}>
            <Text style={styles.segmentTitle}>Produto</Text>
            <View style={styles.segment}>
              {renderFeatureOption('IPROOV', 'iProov')}
              {renderFeatureOption('SAAS', 'SaaS')}
            </View>

            {isSaas ? (
              <>
                <Text style={styles.segmentTitle}>Provider da jornada</Text>
                <View style={styles.segment}>
                  {renderSaasProviderOption('FACETEC', 'Facetec')}
                  {renderSaasProviderOption('FORTFACE', 'Fortface')}
                </View>
              </>
            ) : null}

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

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Dados do usuário</Text>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>CPF</Text>
              <TextInput
                style={styles.input}
                value={userData.cpf}
                onChangeText={(text) => setUserData({ cpf: maskCpf(text) })}
                placeholder="000.000.000-00"
                keyboardType="numeric"
                maxLength={14}
                placeholderTextColor="#94A3B8"
              />
            </View>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>Nome</Text>
              <TextInput
                style={styles.input}
                value={userData.nome}
                onChangeText={(text) => setUserData({ nome: text })}
                placeholder="Nome completo"
                placeholderTextColor="#94A3B8"
              />
            </View>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>Data de Nascimento</Text>
              <TextInput
                style={styles.input}
                value={userData.nascimento}
                onChangeText={(text) =>
                  setUserData({ nascimento: maskBirthDate(text) })
                }
                placeholder="DD/MM/AAAA"
                keyboardType="numeric"
                maxLength={10}
                placeholderTextColor="#94A3B8"
              />
            </View>
          </View>

          {isSaas ? (
            <View style={styles.section}>
              <Text style={styles.sectionTitle}>Sessão Certiface SAAS</Text>
              {journeyToken ? (
                <View style={styles.tokenBadge}>
                  <Text style={styles.tokenBadgeText}>
                    Token SAAS salvo localmente
                  </Text>
                </View>
              ) : null}

              <View style={styles.inputGroup}>
                <Text style={styles.inputLabel}>Login do operador</Text>
                <TextInput
                  style={styles.input}
                  value={saasOperator.login}
                  onChangeText={(text) => setSaasOperator({ login: text })}
                  autoCapitalize="none"
                  autoCorrect={false}
                  placeholderTextColor="#94A3B8"
                />
              </View>

              <View style={styles.inputGroup}>
                <Text style={styles.inputLabel}>Senha do operador</Text>
                <View style={styles.passwordRow}>
                  <TextInput
                    style={[styles.input, styles.passwordInput]}
                    value={saasOperator.password}
                    onChangeText={(text) => setSaasOperator({ password: text })}
                    secureTextEntry={!showPassword}
                    autoCapitalize="none"
                    autoCorrect={false}
                    placeholderTextColor="#94A3B8"
                  />
                  <TouchableOpacity
                    style={styles.passwordToggle}
                    onPress={() => setShowPassword((prev) => !prev)}
                    accessibilityRole="button"
                    accessibilityLabel={
                      showPassword ? 'Ocultar senha' : 'Mostrar senha'
                    }
                  >
                    <Ionicons
                      name={showPassword ? 'eye-off-outline' : 'eye-outline'}
                      size={22}
                      color="#64748B"
                    />
                  </TouchableOpacity>
                </View>
              </View>

              <View style={styles.inputGroup}>
                <View style={styles.inputHeaderRow}>
                  <Text style={styles.inputLabel}>Journey Token</Text>
                  <TouchableOpacity
                    style={[
                      styles.copyButton,
                      !journeyToken && styles.buttonDisabled,
                    ]}
                    onPress={() => handleCopy(journeyToken, 'Journey Token')}
                    disabled={!journeyToken}
                  >
                    <Text style={styles.copyButtonText}>Copiar</Text>
                  </TouchableOpacity>
                </View>
                <TextInput
                  style={[styles.input, styles.tokenInput]}
                  value={journeyToken}
                  onChangeText={setJourneyToken}
                  placeholder="Cole o journeyToken ou gere um novo"
                  multiline
                  numberOfLines={4}
                  autoCapitalize="none"
                  autoCorrect={false}
                  placeholderTextColor="#94A3B8"
                />
              </View>

              {journeyToken ? (
                <TouchableOpacity
                  style={[styles.clearButton, loading && styles.buttonDisabled]}
                  onPress={handleClearSaas}
                  disabled={loading}
                >
                  <Text style={styles.clearButtonText}>Limpar sessão SAAS</Text>
                </TouchableOpacity>
              ) : null}
            </View>
          ) : (
            <View style={styles.section}>
              <Text style={styles.sectionTitle}>App Key</Text>
              <View style={styles.inputGroup}>
                <View style={styles.inputHeaderRow}>
                  <Text style={styles.inputLabel}>App Key Atual</Text>
                  <TouchableOpacity
                    style={[
                      styles.copyButton,
                      !appKey && styles.buttonDisabled,
                    ]}
                    onPress={() => handleCopy(appKey, 'App Key')}
                    disabled={!appKey}
                  >
                    <Text style={styles.copyButtonText}>Copiar</Text>
                  </TouchableOpacity>
                </View>
                <TextInput
                  style={[styles.input, styles.tokenInput]}
                  value={appKey}
                  onChangeText={setAppKey}
                  placeholder="Cole sua App Key ou gere uma nova"
                  multiline
                  numberOfLines={4}
                  autoCapitalize="none"
                  autoCorrect={false}
                  placeholderTextColor="#94A3B8"
                />
              </View>
            </View>
          )}
        </ScrollView>

        <View style={styles.footer}>
          <TouchableOpacity
            style={[styles.generateButton, loading && styles.buttonDisabled]}
            onPress={handleGenerate}
            disabled={loading}
          >
            {loading ? (
              <View style={styles.loadingContent}>
                <ActivityIndicator color="#1D4ED8" size="small" />
                <Text style={styles.buttonText}>Gerando...</Text>
              </View>
            ) : (
              <Text style={styles.buttonText}>
                {isSaas ? 'Gerar token SAAS' : 'Gerar App Key'}
              </Text>
            )}
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#F1F5F9',
  },
  keyboardAvoidingView: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
  },
  scrollContent: {
    padding: 16,
    paddingBottom: 24,
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
  section: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 16,
    marginBottom: 20,
    shadowColor: '#1E293B',
    shadowOpacity: 0.1,
    shadowRadius: 10,
    shadowOffset: { width: 0, height: 4 },
    elevation: 5,
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
    fontSize: 18,
    fontWeight: '800',
    marginBottom: 15,
    color: '#0F172A',
  },
  inputGroup: {
    marginBottom: 12,
  },
  inputLabel: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 6,
    color: '#334155',
  },
  inputHeaderRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 6,
  },
  input: {
    borderWidth: 1,
    borderColor: '#E2E8F0',
    borderRadius: 12,
    padding: 12,
    fontSize: 14,
    color: '#0F172A',
    backgroundColor: '#F8FAFC',
  },
  passwordRow: {
    position: 'relative',
    justifyContent: 'center',
  },
  passwordInput: {
    paddingRight: 44,
  },
  passwordToggle: {
    position: 'absolute',
    right: 12,
    height: '100%',
    justifyContent: 'center',
    alignItems: 'center',
  },
  tokenInput: {
    minHeight: 84,
    textAlignVertical: 'top',
    fontFamily: 'monospace',
    fontSize: 12,
    backgroundColor: '#EFF6FF',
  },
  tokenBadge: {
    alignSelf: 'flex-start',
    backgroundColor: '#DCFCE7',
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 8,
    marginBottom: 14,
  },
  tokenBadgeText: {
    color: '#15803D',
    fontSize: 12,
    fontWeight: '600',
  },
  generateButton: {
    backgroundColor: '#FFFFFF',
    paddingVertical: 12,
    paddingHorizontal: 14,
    borderRadius: 14,
    alignItems: 'center',
    shadowColor: '#1E293B',
    shadowOpacity: 0.08,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 4,
  },
  clearButton: {
    marginTop: 4,
    paddingVertical: 12,
    alignItems: 'center',
  },
  clearButtonText: {
    color: '#DC2626',
    fontSize: 14,
    fontWeight: '700',
  },
  buttonDisabled: {
    opacity: 0.55,
  },
  buttonText: {
    color: '#1D4ED8',
    fontSize: 15,
    fontWeight: '700',
  },
  loadingContent: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  copyButton: {
    backgroundColor: '#DBEAFE',
    borderRadius: 8,
    paddingHorizontal: 10,
    paddingVertical: 6,
  },
  copyButtonText: {
    color: '#1D4ED8',
    fontSize: 12,
    fontWeight: '700',
  },
  footer: {
    paddingHorizontal: 16,
    paddingBottom: 16,
    paddingTop: 8,
    backgroundColor: '#F1F5F9',
  },
});

export default SessionScreen;
