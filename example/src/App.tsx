import React, { useState } from 'react';
import {
  Text,
  View,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
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
      const appKey =
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjZXJ0aWZhY2UiLCJ1c2VyIjoiRDFEOTY0NzFGOEJGOEZCRkUwODMzMUUzQUVFRDg2NUJFQXxtb2JpbGUuZGVtby5hcHAiLCJlbXBDb2QiOiIwMDAwMDAwNzQyIiwiZmlsQ29kIjoiMDAwMDAwMjk2OCIsImNwZiI6IjYyNzE3NDIzMDAxIiwibm9tZSI6IkFFNTE2NTMzOTdCMjk2RkVFQUVEOUZEMUJEMEM5NDQ2OUMzRkUzMDZBRTQ3MTIwRkZEQjRCRTBBMjFEMUU5NjE1QzdDQ0YxMTI0MTc1RDIwMDlBQ0UxMUE5MDYxQjZFNUFGREUwNUJFM0MyNzNCQUM4ODM5MEZEQTNFODZDNUY0NHxURVNURSBNT0JJTEUgSE9NT0xPRyIsIm5hc2NpbWVudG8iOiIwMS8wMS8yMDAwIiwiZWFzeS1pbmRleCI6IkFBQUFFaW1wT0xyQmhVM3RCUGZVdWZHRWZ0aGNHamhLN1Y0VTNYT3pVTXNOZnpaR1h1S3VrY3NqNjN0OElBPT0iLCJrZXkiOiJTbkV3SEdNd2VSQXhYVmV3SEdBaWRuRGhlblV6SEZtYWQ3MXZIQ1hrTUJBR1BtTD0iLCJleHAiOjE3NTM3ODc1MzYsImlhdCI6MTc1Mzc4NzIzNn0.hCM6kZ7uon_exIiQNRbWW9CFiXOXv_MizcUVIc8B9_I';
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
    <View style={styles.container}>
      <Text style={styles.title}>RnSdk Example</Text>

      <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.button} onPress={handleMultiply}>
          <Text style={styles.buttonText}>Test Multiply</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.button} onPress={handleCheckPermission}>
          <Text style={styles.buttonText}>Check Camera Permission</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.button}
          onPress={handleRequestPermission}
        >
          <Text style={styles.buttonText}>Request Camera Permission</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.button} onPress={handleStartJourney}>
          <Text style={styles.buttonText}>Start Journey</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.clearButton} onPress={clearResults}>
          <Text style={styles.buttonText}>Clear Results</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.clearButton}
          onPress={() => testString('test')}
        >
          <Text style={styles.buttonText}>Test</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.resultsContainer}>
        <Text style={styles.resultsTitle}>Results:</Text>
        <ScrollView style={styles.resultsScroll}>
          {results.length === 0 ? (
            <Text style={styles.noResults}>
              No results yet. Try the buttons above!
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
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#f5f5f5',
  },
  title: {
    fontSize: 24,
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
    flex: 1,
    backgroundColor: 'white',
    borderRadius: 8,
    padding: 15,
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
