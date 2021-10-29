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

export type HgwBean = {
  ip: string;
  gwId: string;
  active: number;
  ability: number;
  lastSeenTime: number;
  mode: number;
  encrypt: boolean;
  productKey: string;
  version: string;
  token: boolean;
  wf_cfg: boolean;
};

export type HgwInformation = {
  gw_id: string;
  product_id: string;
};

export type InitGwActivatorParams = {
  homeId: number;
  time: number;
};

export type SearchedGwActivatorParams = {
  homeId: number;
  time: number;
  devId: string; // for android
  gwId: string; // for iOS
  productId?: string;
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

export function StartSearcingGwDevice() {
  tuya.StartSearcingGwDevice();
  return;
}

export function InitSearchedGwDevice(
  params: SearchedGwActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.InitSearchedGwDevice(params);
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

export function newGwSubDevActivator(
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

  return tuya.newGwSubDevActivator(passParameter);
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
