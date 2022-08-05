import * as yup from 'yup';

import { getTranslatedText } from 'components/local/localization';
import { NO_SPACE, NAME_REG_EXP, PHONE_REG_EXP } from 'config';

const errorMessage = (validatedObject, message) =>
  validatedObject.value.length > 0 ? message : undefined;

export const validationUserSchema = ({
  lastName,
  firstName,
  phoneForInitial,
}) =>
  yup.object().shape({
    firstName: yup
      .string()
      .min(2, (obj) => errorMessage(obj, getTranslatedText('errors.min2')))
      .max(50, (obj) => errorMessage(obj, getTranslatedText('errors.max50')))
      .matches(NO_SPACE, (obj) =>
        errorMessage(obj, getTranslatedText('errors.noSpace'))
      )
      .matches(NAME_REG_EXP, (obj) =>
        errorMessage(obj, getTranslatedText('errors.nameMatch'))
      )
      .default(() => firstName),
    lastName: yup
      .string()
      .min(2, (obj) => errorMessage(obj, getTranslatedText('errors.min2')))
      .max(50, (obj) => errorMessage(obj, getTranslatedText('errors.max50')))
      .matches(NO_SPACE, (obj) =>
        errorMessage(obj, getTranslatedText('errors.noSpace'))
      )
      .matches(NAME_REG_EXP, (obj) =>
        errorMessage(obj, getTranslatedText('errors.nameMatch'))
      )
      .default(() => lastName),
    phones: yup
      .array()
      .of(
        yup
          .string()
          .matches(PHONE_REG_EXP, getTranslatedText('errors.phoneMatch'))
      )
      .default(() => phoneForInitial),
  });