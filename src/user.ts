import { NativeModules } from 'react-native';

const tuya = NativeModules.TuyaUserModule;

export const user = {
  registerAccountWithEmail(
    params: RegisterAccountWithEmailParams
  ): Promise<any> {
    return tuya.registerAccountWithEmail(params);
  },
  getRegisterEmailValidateCode(
    params: GetEmailValidateCodeParams
  ): Promise<any> {
    return tuya.getRegisterEmailValidateCode(params);
  },
  loginWithEmail(params: LoginWithEmailParams): Promise<any> {
    return tuya.loginWithEmail(params);
  },
  getEmailValidateCode(params: GetEmailValidateCodeParams): Promise<any> {
    return tuya.getEmailValidateCode(params);
  },
  resetEmailPassword(params: ResetEmailPasswordParams): Promise<any> {
    return tuya.resetEmailPassword(params);
  },
  logout(): Promise<string> {
    return tuya.logout();
  },
  getCurrentUser() {
    // TODO: provide typings
    return tuya.getCurrentUser();
  },
  cancelAccount(): Promise<string> {
    return tuya.cancelAccount();
  },
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
