import { Title } from 'obminyashka-components';

import { getTranslatedText } from 'src/components/local/localization';

import { Children } from './children';
import { AboutMyself } from './about-myself';

const MyProfile = () => {
  return (
    <>
      <Title style={{ margin: '65px 0 40px' }} text={getTranslatedText('ownInfo.aboutMe')} />

      <AboutMyself />

      <Title style={{ margin: '65px 0 40px' }} text={getTranslatedText('ownInfo.children')} />

      <Children />
    </>
  );
};
export default MyProfile;
