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

// Checked by Using
export function addEvent(
  eventName: string,
  callback: (data: any) => any
): EmitterSubscription {
  return eventEmitter.addListener(eventName, callback);
}

// Checked by Using
export function removeEvent(eventName: string): void {
  eventEmitter.removeAllListeners(eventName);
  return;
}

// Checked by Using
export function removeSubscribtion(subscription: EmitterSubscription): void {
  subscription.remove();
  return;
}

export const bridge = (key: string, id: string | number) => `${key}//${id}`;
