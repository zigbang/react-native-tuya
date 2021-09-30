import { NativeModules } from 'react-native';

const tuya = NativeModules.TuyaUserModule;

export function registerAccountWithEmail(
  params: RegisterAccountWithEmailParams
): Promise<any> {
  return tuya.registerAccountWithEmail(params);
}

export function getRegisterEmailValidateCode(
  params: GetEmailValidateCodeParams
): Promise<any> {
  return tuya.getRegisterEmailValidateCode(params);
}

export function loginWithEmail(params: LoginWithEmailParams): Promise<any> {
  return tuya.loginWithEmail(params);
}

export function getEmailValidateCode(
  params: GetEmailValidateCodeParams
): Promise<any> {
  return tuya.getEmailValidateCode(params);
}

export function resetEmailPassword(
  params: ResetEmailPasswordParams
): Promise<any> {
  return tuya.resetEmailPassword(params);
}

export function logout(): Promise<string> {
  return tuya.logout();
}

export async function getCurrentUser(): Promise<User | null> {
  const user = await tuya.getCurrentUser();
  // The iOS SDK returns an empty user model but the Android one doesn't.
  return user && user.email ? user : null;
}

export function cancelAccount(): Promise<string> {
  return tuya.cancelAccount();
}

export type User = {
  email: string;
  username: string;
  sid: string;
  timezoneId: string;
  uid: string;
  userType: number;
  headPic: string;
  mobile: string;
  nickName: string;
  phoneCode: string;
};

export type RegisterAccountWithEmailParams = {
  countryCode: string;
  email: string;
  validateCode: string;
  password: string;
};

export type GetEmailValidateCodeParams = {
  countryCode: string;
  email: string;
};

export type LoginWithEmailParams = {
  email: string;
  password: string;
  countryCode: string;
};

export type ResetEmailPasswordParams = {
  email: string;
  countryCode: string;
  validateCode: string;
  newPassword: string;
};
