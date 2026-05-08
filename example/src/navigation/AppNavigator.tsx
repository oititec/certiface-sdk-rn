import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Platform } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import SessionScreen from '../screens/SessionScreen';
import HomeScreen from '../screens/HomeScreen';
import ResultScreen from '../screens/ResultScreen';
import BottomTabIcon from '../components/BottomTabIcon';

export type RootTabParamList = {
  Home: undefined;
  Credential: undefined;
  Results: undefined;
};

const Tab = createBottomTabNavigator<RootTabParamList>();

const AppNavigator = () => {
  const insets = useSafeAreaInsets();
  const tabBarBottomPadding =
    Platform.OS === 'ios'
      ? Math.max(insets.bottom, 20)
      : Math.max(insets.bottom, 8);
  const tabBarHeight = (Platform.OS === 'ios' ? 64 : 56) + tabBarBottomPadding;

  return (
    <NavigationContainer>
      <Tab.Navigator
        initialRouteName="Home"
        screenOptions={({ route }) => ({
          tabBarActiveTintColor: '#2563EB',
          tabBarInactiveTintColor: '#64748B',
          tabBarLabelStyle: { fontSize: 11, fontWeight: '700', marginTop: 2 },
          tabBarItemStyle: {
            height: '100%',
            paddingVertical: 0,
            justifyContent: 'center',
            alignItems: 'center',
          },
          unmountOnBlur: false,
          tabBarStyle: {
            height: tabBarHeight,
            backgroundColor: '#FFFFFF',
            borderTopWidth: 1,
            borderTopColor: '#E2E8F0',
            paddingTop: 6,
            paddingBottom: tabBarBottomPadding,
            elevation: 0,
            shadowOpacity: 0,
          },
          sceneStyle: { backgroundColor: '#F1F5F9' },
          headerStyle: {
            backgroundColor: '#F1F5F9',
            elevation: 0,
            shadowOpacity: 0,
          },
          headerTintColor: '#0F172A',
          headerTitleStyle: { fontWeight: '800', fontSize: 20 },
          tabBarIcon: ({ color, focused }) => {
            let iconName: 'home' | 'key' | 'document' = 'home';
            if (route.name === 'Home') {
              iconName = 'home';
            }
            if (route.name === 'Credential') {
              iconName = 'key';
            }
            if (route.name === 'Results') {
              iconName = 'document';
            }
            return (
              <BottomTabIcon name={iconName} color={color} focused={focused} />
            );
          },
        })}
      >
        <Tab.Screen
          name="Home"
          component={HomeScreen}
          options={{ title: 'Home', tabBarLabel: 'Home' }}
        />
        <Tab.Screen
          name="Credential"
          component={SessionScreen}
          options={{ title: 'Credencial', tabBarLabel: 'Credencial' }}
        />
        <Tab.Screen
          name="Results"
          component={ResultScreen}
          options={{ title: 'Resultados', tabBarLabel: 'Resultados' }}
        />
      </Tab.Navigator>
    </NavigationContainer>
  );
};

export default AppNavigator;
