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
import { Environment } from '@certiface/sdk';
import { useNavigation } from '@react-navigation/native';
import type { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import type { RootTabParamList } from '../navigation/AppNavigator';
import { useUserStore } from '../store/userStore';

type SessionNavigationProp = BottomTabNavigationProp<
  RootTabParamList,
  'Credential'
>;

const SessionScreen = () => {
  const navigation = useNavigation<SessionNavigationProp>();
  const {
    userData,
    appKey,
    setUserData,
    setAppKey,
    generateAppKey,
    selectedFeature,
    environment,
    setSelectedFeature,
    setEnvironment,
    addResult,
  } = useUserStore();
  const [loading, setLoading] = useState(false);

  const handleCopyAppKey = () => {
    if (!appKey) {
      return;
    }
    Clipboard.setString(appKey);
    Alert.alert('Copiado', 'App Key copiada para a área de transferência.');
  };

  const handleGenerateAppKey = async () => {
    try {
      setLoading(true);
      const generated = await generateAppKey();
      addResult(`AppKey gerada: ${generated}`);
      navigation.navigate('Home');
    } catch (error) {
      Alert.alert('Erro', `Falha ao gerar App Key: ${error}`);
      addResult(`ERRO: Falha ao gerar App Key: ${error}`);
      navigation.navigate('Results');
    } finally {
      setLoading(false);
    }
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
          <View style={styles.header}>
            <Text style={styles.title}>Credencial</Text>
            <Text style={styles.subtitle}>Informe ou gere sua App Key</Text>
          </View>

          <View style={styles.section}>
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

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Dados do Usuário</Text>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>CPF</Text>
              <TextInput
                style={styles.input}
                value={userData.cpf}
                onChangeText={(text) => setUserData({ cpf: text })}
                placeholder="000.000.000-00"
                keyboardType="numeric"
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
                onChangeText={(text) => setUserData({ nascimento: text })}
                placeholder="DD/MM/AAAA"
                keyboardType="numeric"
                placeholderTextColor="#94A3B8"
              />
            </View>

            <View style={styles.inputGroup}>
              <View style={styles.inputHeaderRow}>
                <Text style={styles.inputLabel}>App Key Atual</Text>
                <TouchableOpacity
                  style={[styles.copyButton, !appKey && styles.buttonDisabled]}
                  onPress={handleCopyAppKey}
                  disabled={!appKey}
                >
                  <Text style={styles.copyButtonText}>Copiar</Text>
                </TouchableOpacity>
              </View>
              <TextInput
                style={[styles.input, styles.appKeyInput]}
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
        </ScrollView>

        <View style={styles.footer}>
          <TouchableOpacity
            style={[styles.generateButton, loading && styles.buttonDisabled]}
            onPress={handleGenerateAppKey}
            disabled={loading}
          >
            {loading ? (
              <View style={styles.loadingContent}>
                <ActivityIndicator color="#1D4ED8" size="small" />
                <Text style={styles.buttonText}>Gerando...</Text>
              </View>
            ) : (
              <Text style={styles.buttonText}>Gerar App Key</Text>
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
  appKeyInput: {
    minHeight: 84,
    textAlignVertical: 'top',
    fontFamily: 'monospace',
    fontSize: 12,
    backgroundColor: '#EFF6FF',
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
