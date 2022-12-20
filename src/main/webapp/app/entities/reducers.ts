import userAccount from 'app/entities/user-account/user-account.reducer';
import post from 'app/entities/post/post.reducer';
import expertUser from 'app/entities/expert-user/expert-user.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userAccount,
  post,
  expertUser,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
