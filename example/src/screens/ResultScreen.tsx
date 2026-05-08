import Clipboard from '@react-native-clipboard/clipboard';
import {
  Alert,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { useUserStore } from '../store/userStore';

const ResultScreen = () => {
  const { results, clearResults } = useUserStore();

  const getTypeStyle = (result: string) => {
    if (result.includes('✅') || result.includes('Sucesso')) {
      return styles.successItem;
    }
    if (result.includes('Erro') || result.includes('ERRO')) {
      return styles.errorItem;
    }
    return styles.infoItem;
  };

  const isAppKeyResult = (result: string) => result.includes('AppKey gerada:');

  const extractAppKey = (result: string) => {
    const marker = 'AppKey gerada:';
    const idx = result.indexOf(marker);
    return idx >= 0 ? result.slice(idx + marker.length).trim() : '';
  };

  const copyAppKey = (result: string) => {
    const appKey = extractAppKey(result);
    if (!appKey) {
      return;
    }
    Clipboard.setString(appKey);
    Alert.alert('Copiado', 'App Key copiada para a área de transferência.');
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <View style={styles.header}>
        <Text style={styles.title}>Resultados</Text>
        <Text style={styles.subtitle}>{results.length} registro(s)</Text>
      </View>

      <ScrollView
        contentContainerStyle={styles.content}
        showsVerticalScrollIndicator={false}
      >
        {results.length === 0 ? (
          <View style={styles.emptyState}>
            <Text style={styles.emptyTitle}>Sem resultados ainda</Text>
            <Text style={styles.emptyText}>
              Execute os testes na Home para acompanhar as execuções aqui.
            </Text>
          </View>
        ) : (
          results.map((result, index) => (
            <View
              key={`${result}-${index}`}
              style={[styles.resultItem, getTypeStyle(result)]}
            >
              <View style={styles.resultRow}>
                <Text style={styles.resultText}>{result}</Text>
                {isAppKeyResult(result) ? (
                  <TouchableOpacity
                    style={styles.copyButton}
                    onPress={() => copyAppKey(result)}
                  >
                    <Text style={styles.copyButtonText}>Copiar</Text>
                  </TouchableOpacity>
                ) : null}
              </View>
            </View>
          ))
        )}
      </ScrollView>

      <View style={styles.footer}>
        <TouchableOpacity
          style={[
            styles.clearButton,
            results.length === 0 && styles.clearButtonDisabled,
          ]}
          onPress={clearResults}
          disabled={results.length === 0}
        >
          <Text style={styles.clearButtonText}>Limpar histórico</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#F1F5F9',
  },
  header: {
    marginHorizontal: 16,
    marginTop: 16,
    borderRadius: 18,
    paddingHorizontal: 18,
    paddingVertical: 16,
    backgroundColor: '#1E3A8A',
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
  clearButton: {
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    paddingHorizontal: 14,
    paddingVertical: 12,
    alignItems: 'center',
    shadowColor: '#1E293B',
    shadowOpacity: 0.08,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 4,
  },
  clearButtonDisabled: {
    opacity: 0.55,
  },
  clearButtonText: {
    color: '#DC2626',
    fontWeight: '700',
  },
  content: {
    padding: 16,
    paddingBottom: 24,
    gap: 10,
  },
  footer: {
    paddingHorizontal: 16,
    paddingBottom: 16,
    paddingTop: 8,
    backgroundColor: '#F1F5F9',
  },
  emptyState: {
    marginTop: 70,
    alignItems: 'center',
    paddingHorizontal: 24,
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    paddingVertical: 28,
    shadowColor: '#1E293B',
    shadowOpacity: 0.08,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 4,
  },
  emptyTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#374151',
    marginBottom: 8,
  },
  emptyText: {
    fontSize: 14,
    color: '#6B7280',
    textAlign: 'center',
    lineHeight: 20,
  },
  resultItem: {
    borderRadius: 12,
    padding: 14,
    borderWidth: 1.5,
    shadowColor: '#1E293B',
    shadowOpacity: 0.07,
    shadowRadius: 6,
    shadowOffset: { width: 0, height: 3 },
    elevation: 3,
  },
  infoItem: {
    backgroundColor: '#EFF6FF',
    borderColor: '#BFDBFE',
  },
  successItem: {
    backgroundColor: '#ECFDF5',
    borderColor: '#86EFAC',
  },
  errorItem: {
    backgroundColor: '#FEF2F2',
    borderColor: '#FCA5A5',
  },
  resultText: {
    flex: 1,
    color: '#111827',
    fontSize: 13,
    lineHeight: 18,
  },
  resultRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  copyButton: {
    backgroundColor: '#1D4ED8',
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 8,
  },
  copyButtonText: {
    color: '#FFFFFF',
    fontSize: 12,
    fontWeight: '700',
  },
});

export default ResultScreen;
