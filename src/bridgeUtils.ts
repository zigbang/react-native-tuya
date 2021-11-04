import {
  NativeEventEmitter,
  NativeModules,
  EmitterSubscription,
} from 'react-native';

export const GROUPLISTENER = 'groupListener';
export const HARDWAREUPGRADELISTENER = 'hardwareUpgradeListener';
export const DEVLISTENER = 'devListener';
export const SUBDEVLISTENER = 'subDevListener';
export const HOMESTATUS = 'homeStatus';
export const HOMECHANGE = 'homeChange';
export const SINGLETRANSFER = 'SingleTransfer';

let eventEmitter = new NativeEventEmitter(NativeModules.TuyaRNEventEmitter);

export function addEvent(eventName: string, callback: (data: any) => any) {
  return eventEmitter.addListener(eventName, callback);
}

export function removeEvent(eventName: string) {
  return eventEmitter.removeAllListeners(eventName);
}

export function removeSubscribtion(subscription: EmitterSubscription) {
  return eventEmitter.removeSubscription(subscription);
}

export const bridge = (key: string, id: string | number) => `${key}//${id}`;
