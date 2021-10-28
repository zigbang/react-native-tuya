import { NativeModules } from 'react-native';
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

export type InitGwActivatorParams = {
  homeId: number;
  time: number;
};

export type SearchedGwActivatorParams = {
  homeId: number;
  time: number;
  devId: string;
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

export function GetFirstSearcingGwDevice(): boolean {
  return tuya.GetFirstSearcingGwDevice() ? true : false;
}

export function InitSearchedGwDevice(
  params: SearchedGwActivatorParams
): Promise<HgwBean> {
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

export function newGwSubDevActivator(
  params: RegistSubForGwParams
): Promise<DeviceDetailResponse> {
  return tuya.newGwSubDevActivator(params);
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
