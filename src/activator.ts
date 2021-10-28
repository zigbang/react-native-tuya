import { NativeModules, Platform } from 'react-native';
import { DeviceDetailResponse } from './home';

const tuya = NativeModules.TuyaActivatorModule;

export function openNetworkSettings() {
  return tuya.openNetworkSettings({});
}

export type InitActivatorParams = {
  homeId: number;
  ssid: string;
  password: string;
  time: number;
  type: 'TY_EZ' | 'TY_AP' | 'TY_QR';
};

export type InitGwActivatorParams = {
  homeId: number;
  time: number;
};

export type RegistSubForGwParams = {
  deviceId: string;
  time: number;
};

export function initActivator(
  params: InitActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.initActivator(params);
}

export function initWiredGwActivator(
  params: InitGwActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.initWiredGwActivator(params);
}

export type registerWiredGWParams = {
  homeId: number;
  gwId: string;
  productId?: string;
  time: number;
};

export function registerWiredGW(
  params: registerWiredGWParams
): Promise<DeviceDetailResponse> {
  return tuya.registerWiredGW(params);
}

export type initWiredGwActivatorByPaaSParams = {
  token: string;
  time: number;
};

export function initWiredGwActivatorByPaaS(
  params: initWiredGwActivatorByPaaSParams
): Promise<DeviceDetailResponse> {
  return tuya.initWiredGwActivatorByPaaS(params);
}

export function startSearchWiredGW() {
  return tuya.startSearchWiredGW();
}

export function registerZigbeeSubDevice(
  params: RegistSubForGwParams
): Promise<DeviceDetailResponse> {
  let passParameter;

  if (Platform.OS === 'ios') {
    passParameter = {
      deviceId: params.deviceId, // deviceId
      time: params.time,
    };
  } else {
    passParameter = {
      devId: params.deviceId, // devId
      time: params.time,
    };
  }

  return tuya.registerZigbeeSubDevice(passParameter);
}

export function stopConfig() {
  return tuya.stopConfig();
}

export function getCurrentWifi(
  success: (ssid: string) => void,
  error: () => void
) {
  // We need the Allow While Using App location permission to use this.
  return tuya.getCurrentWifi({}, success, error);
}
