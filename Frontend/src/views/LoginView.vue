<script setup>
import { ref } from 'vue';
import router from '../router/index'
import { RouterLink } from 'vue-router';
import { host } from '../config';

const username = ref('');
const password = ref('');


const login = async () => {
  const response = await fetch(host + "/login", {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      username: username.value,
      password: password.value,
    })
  })
  const result = await response.text()
  if (response.ok) {
    router.push('/builder')
  } else {
    alert(result)
  }
};
</script>

<template>
  <main class="loginComonent">
    <div id="loginComp"
      style="background-color: #fff; display: flex; justify-content: center; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
      <div class="cardContainer">
        <form @submit.prevent="login" id="credentials">
        <div style="text-align: center;">
          <label class="loginLabel" for="username">Username</label>
          <input v-model="username" type="text" name="username" id="username"
            style="padding: 8px; margin: 8px; border: 1px solid #ccc; border-radius: 4px;">
          <label class="loginLabel" for="password">Password</label>
          <input v-model="password" type="password" name="password" id="password"
            style="padding: 8px; margin: 8px; border: 1px solid #ccc; border-radius: 4px;">
          <button type="submit">Sign in</button>
        </div>
      </form>
        <p class="miniURL" style="font-size: 0.80rem;">
          <RouterLink to="register">Register here</RouterLink>
        </p>
      </div>
    </div>
  </main>
</template>

<style scoped>
.loginLabel {
  color: black;
}


</style>