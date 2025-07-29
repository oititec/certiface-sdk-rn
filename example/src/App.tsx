import React, { useState } from 'react';
import {
  Text,
  View,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  TextInput,
  SafeAreaView,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import {
  multiply,
  checkCameraPermission,
  requestCameraPermission,
  startJourney,
  testString,
} from '@oiti/rn-sdk';

export default function App() {
  const [results, setResults] = useState<string[]>([]);
  const [appKey, setAppKey] = useState<string>('');

  const addResult = (message: string) => {
    setResults((prev) => [
      ...prev,
      `${new Date().toLocaleTimeString()}: ${message}`,
    ]);
  };

  const handleMultiply = () => {
    const result = multiply(3, 7);
    addResult(`Multiply result: ${result}`);
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
    try {
      const result = await startJourney(appKey);
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
          <Text style={styles.title}>RN OitiSDK Example</Text>

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
              style={styles.button}
              onPress={handleStartJourney}
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

          <View style={styles.appKeyContainer}>
            <Text style={styles.appKeyLabel}>App Key:</Text>
            <TextInput
              style={styles.appKeyInput}
              value={appKey}
              onChangeText={setAppKey}
              placeholder="Cole a chave da aplicação aqui!"
              multiline
              numberOfLines={3}
            />
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

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
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 20,
    color: '#333',
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
  appKeyContainer: {
    backgroundColor: 'white',
    borderRadius: 8,
    padding: 15,
  },
  appKeyLabel: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#333',
  },
  appKeyInput: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 6,
    padding: 12,
    fontSize: 14,
    color: '#333',
    backgroundColor: '#f9f9f9',
    textAlignVertical: 'top',
  },
});
