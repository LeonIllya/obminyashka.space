import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Formik } from 'formik';
import * as yup from 'yup';

import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localisation';
import Button from 'components/common/buttons/button/Button';
import CheckBox from 'components/common/checkbox';
import InputForAuth from 'components/common/input';
import { NO_SPACE } from 'config';

import SpinnerForAuthBtn from 'components/common/spinner/spinnerForAuthBtn';
import { Extra, ExtraLink } from './loginStyle';
import { putUserThunk } from 'store/auth/thunk';

const Login = () => {
  const dispatch = useDispatch();
  const { lang } = useSelector((state) => state.auth);
  const [checkbox, setCheckbox] = useState(false);
  const [loading, setLoading] = useState(false);

  const changeCheckBox = () => {
    setCheckbox((prev) => !prev);
  };

  const validationLoginSchema = yup.object().shape({
    usernameOrEmail: yup
      .string()
      .required(getTranslatedText('errors.requireField', lang))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace', lang))
      .default(() => ''),
    password: yup
      .string()
      .required(getTranslatedText('errors.requireField', lang))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace', lang))
      .default(() => ''),
  });
  const initialLoginValues = validationLoginSchema.cast({});

  const onSubmitHandler = async (values, onSubmitProps) => {
    if (loading) return;
    setLoading(true);
    try {
      setLoading(false);
      await dispatch(putUserThunk(values, checkbox));
    } catch (err) {
      setLoading(false);
      if (err.response.status === 400) {
        onSubmitProps.setErrors({
          usernameOrEmail: err.response.data.error,
        });
      }
    }
  };

  return (
    <form>
      <Formik
        initialValues={initialLoginValues}
        validationSchema={validationLoginSchema}
        validateOnBlur
        enableReinitialize
        onSubmit={async (values, onSubmitProps) =>
          onSubmitHandler(values, onSubmitProps)
        }
      >
        {({ errors, handleSubmit, isValid, dirty }) => {
          return (
            <>
              <div>
                <InputForAuth
                  text={getTranslatedText('auth.logEmail', lang)}
                  name="usernameOrEmail"
                  type="text"
                />
                <InputForAuth
                  text={getTranslatedText('auth.logPassword', lang)}
                  name="password"
                  type="password"
                />
              </div>
              <Extra>
                <CheckBox
                  text={getTranslatedText('auth.remember', lang)}
                  margin="0 0 44px 0"
                  fontSize="14px"
                  checked={checkbox}
                  click={changeCheckBox}
                />
                <ExtraLink to={`${route.login}${route.signUp}`}>
                  {getTranslatedText('auth.noLogin', lang)}
                </ExtraLink>
              </Extra>
              <Button
                text={
                  loading ? (
                    <SpinnerForAuthBtn />
                  ) : (
                    getTranslatedText('button.enter', lang)
                  )
                }
                bold
                height="48px"
                lHeight="24px"
                mb="64px"
                type="submit"
                width="222px"
                disabling={!isValid && !dirty}
                click={!errors.usernameOrEmail ? handleSubmit : null}
              />
            </>
          );
        }}
      </Formik>
    </form>
  );
};

export default Login;
