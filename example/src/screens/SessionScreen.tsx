import { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  Alert,
  KeyboardAvoidingView,
  Platform,
  Switch,
} from 'react-native';
import { LivenessProvider } from '@oiti/rn-sdk';
import { useUserStore } from '../store/userStore';

const SessionScreen = () => {
  const {
    userData,
    appKey,
    setUserData,
    generateAppKey,
    livenessProvider,
    setProvider: setStoreProvider,
  } = useUserStore();
  const [loading, setLoading] = useState(false);
  const [localProvider, setLocalProvider] = useState<LivenessProvider>(
    livenessProvider === 'IPROOV' ? LivenessProvider.IPROOV : LivenessProvider.FACETEC
  );

  const handleProviderChange = (value: boolean) => {
    const newProvider = value
      ? LivenessProvider.IPROOV
      : LivenessProvider.FACETEC;
    setLocalProvider(newProvider);
    setStoreProvider(newProvider);
  };

  const handleGenerateAppKey = async () => {
    try {
      setLoading(true);
      await generateAppKey();
      Alert.alert('Sucesso', 'App Key gerada com sucesso!');
    } catch (error) {
      Alert.alert('Erro', `Falha ao gerar App Key: ${error}`);
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
          <View style={styles.switchContainer}>
            <Text style={styles.switchLabel}>Provider:</Text>
            <Switch
              value={localProvider === LivenessProvider.IPROOV}
              onValueChange={handleProviderChange}
              trackColor={{ false: '#767577', true: '#4A90E2' }}
              thumbColor={
                localProvider === LivenessProvider.IPROOV
                  ? '#FFFFFF'
                  : '#f4f3f4'
              }
            />
            <Text style={styles.switchStatus}>{localProvider}</Text>
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Dados do Usuário</Text>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>CPF:</Text>
              <TextInput
                style={styles.input}
                value={userData.cpf}
                onChangeText={(text) => setUserData({ cpf: text })}
                placeholder="000.000.000-00"
                keyboardType="numeric"
              />
            </View>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>Nome:</Text>
              <TextInput
                style={styles.input}
                value={userData.nome}
                onChangeText={(text) => setUserData({ nome: text })}
                placeholder="Nome completo"
              />
            </View>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>Data de Nascimento:</Text>
              <TextInput
                style={styles.input}
                value={userData.nascimento}
                onChangeText={(text) => setUserData({ nascimento: text })}
                placeholder="DD/MM/AAAA"
                keyboardType="numeric"
              />
            </View>
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>App Key</Text>

            <TouchableOpacity
              style={[styles.generateButton, loading && styles.buttonDisabled]}
              onPress={handleGenerateAppKey}
              disabled={loading}
            >
              <Text style={styles.buttonText}>
                {loading ? 'Gerando...' : 'Gerar App Key'}
              </Text>
            </TouchableOpacity>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>App Key Atual:</Text>
              <TextInput
                style={[styles.input, styles.appKeyInput]}
                value={appKey}
                placeholder="App Key será gerada aqui"
                multiline
                numberOfLines={4}
                editable={false}
              />
            </View>
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
  },
  scrollContent: {
    padding: 20,
    paddingBottom: 40,
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
  section: {
    backgroundColor: 'white',
    borderRadius: 8,
    padding: 15,
    marginBottom: 20,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 15,
    color: '#333',
  },
  inputGroup: {
    marginBottom: 15,
  },
  inputLabel: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 5,
    color: '#333',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 6,
    padding: 12,
    fontSize: 14,
    color: '#333',
    backgroundColor: '#f9f9f9',
  },
  appKeyInput: {
    minHeight: 100,
    textAlignVertical: 'top',
    fontFamily: 'monospace',
    fontSize: 12,
    backgroundColor: '#f0f0f0',
  },
  generateButton: {
    backgroundColor: '#0F9D58',
    padding: 15,
    borderRadius: 8,
    marginBottom: 15,
    alignItems: 'center',
  },
  buttonDisabled: {
    backgroundColor: '#cccccc',
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
});

export default SessionScreen;
