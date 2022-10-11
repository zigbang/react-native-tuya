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

export type newWifiEzActivatorParams = {
  homeId: number;
  ssid: string;
  password: string;
  time: number;
};

export type GetTokenParams = {
  homeId: number;
};

export type newWifiApActivatorParams = {
  homeId: number;
  ssid: string;
  password: string;
  time: number;
  token: string;
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
  devId: string;
  time: number;
};

export type stopNewGwSubDevActivatorConfigParams = {
  devId: string;
};

export function initActivator(
  params: InitActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.initActivator(params);
}

// Add new for Wifi Ez Device
export function newWifiEzActivator(
  params: newWifiEzActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.initWifiEzDeviceActivator(params);
}

// Add new for get Token (Wifi Ap Device)
export function getTokenForActivator(params: GetTokenParams): Promise<string> {
  return tuya.getTokenForActivator(params);
}

// Add new for Wifi Ap Device
export function newWifiApActivator(
  params: newWifiApActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.initWifiApDeviceActivator(params);
}

export function initWiredGwActivator(
  params: InitGwActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.initWiredGwActivator(params);
}

// Checked by Using
export function startSearcingGwDevice() {
  tuya.StartSearcingGwDevice();
}

// Checked by Using
export function initSearchedGwDevice(
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
  return tuya.newGwSubDevActivator(params);
}

// Checked by Using
export function startGwSubDevActivator(
  params: RegistSubForGwParams
): Promise<boolean> {
  if (Platform.OS === 'ios') {
    return tuya.newGwSubDevActivator(params);
  } else {
    return tuya.StartGwSubDevActivator(params);
  }
}

// Checked by Using (iOS only)
export function stopNewGwSubDevActivatorConfig(
  params: stopNewGwSubDevActivatorConfigParams
) {
  return tuya.stopNewGwSubDevActivatorConfig(params);
}

// Checked by Using
export function startWifiDevActivator(
  params: RegistSubForGwParams
): Promise<boolean> {
  if (Platform.OS === 'ios') {
    return tuya.startWifiDevActivator(params); // ? not sure
  } else {
    return tuya.startWifiDevActivator(params);
  }
}

// Checked by Using
export function stopWifiDevActivatorConfig(
  params: stopNewGwSubDevActivatorConfigParams
) {
  return tuya.stopNewGwSubDevActivatorConfig(params);
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
