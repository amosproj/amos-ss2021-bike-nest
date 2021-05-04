import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { CreateAccountScreen } from './screens/CreateAccountScreen';
import PersonalInformationScreen from './screens/PersonalInformationScreen';


export default function App() {
  return (
    <NavigationContainer>
      <AppNavigator.Navigator initialRouteName="CreateAccountScreen">
        <AppNavigator.Screen name='CreateAccount' component={CreateAccountScreen} />
        <AppNavigator.Screen name='EditPersonalInformation' component={PersonalInformationScreen} />
      </AppNavigator.Navigator>
    </NavigationContainer>
  );
}

const AppNavigator = createStackNavigator();


