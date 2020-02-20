import { NativeModules, EmitterSubscription } from 'react-native';
import { addEvent, bridge, DEVLISTENER } from './bridgeUtils';

const tuya = NativeModules.TuyaDeviceModule;

export type DevListenerParams = {
  devId: string;
};

export type DevListenerType =
  | 'onDpUpdate'
  | 'onRemoved'
  | 'onStatusChanged'
  | 'onNetworkStatusChanged'
  | 'onDevInfoUpdate'
  | 'onFirmwareUpgradeSuccess'
  | 'onFirmwareUpgradeFailure'
  | 'onFirmwareUpgradeProgress';

export function registerDevListener(
  params: DevListenerParams,
  type: DevListenerType,
  callback: (data: any) => void
) {
  tuya.registerDevListener(params);
  return addEvent(bridge(DEVLISTENER, params.devId), data => {
    if (data.type === type) {
      callback(data);
    }
  });
}

export function unRegisterDevListener(
  params: DevListenerParams,
  sub: EmitterSubscription
) {
  tuya.unRegisterDevListener(params);
  sub.remove();
}

export type DeviceDpValue = boolean | number | string;
export type DeviceDps = {
  [dpId: string]: DeviceDpValue;
};
export type SendParams = {
  devId: string;
} & DeviceDps;

export function send(params: object) {
  return tuya.send(params);
}

export type RemoveDeviceParams = { devId: string };

export function removeDevice(params: RemoveDeviceParams): Promise<string> {
  return tuya.removeDevice(params);
}

export type RenameDeviceParams = { devId: string; name: string };

export function renameDevice(params: RenameDeviceParams): Promise<string> {
  return tuya.renameDevice(params);
}

export type GetDataPointStatsParams = {
  devId: string;
  DataPointTypeEnum: 'DAY' | 'WEEK' | 'MONTH';
  number: number; // number of historical data result values, up to 50
  dpId: string;
  startTime: number; // in ms
};

export function getDataPointStat(
  params: GetDataPointStatsParams
): Promise<any> {
  return tuya.getDataPointStat(params);
}
