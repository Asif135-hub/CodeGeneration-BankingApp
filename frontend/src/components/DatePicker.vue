<template>
  <div>
    <label for="inputDOB" class="form-label form-text-lg">Date of Birth</label>
    <datepicker
        v-model="dob"
        id="inputDOB"
        class="form-control"
        :format="dateFormat"
        :input-class="'form-control'"
        :clear-button="true"
        :placeholder="'Select Date'"
        required
    ></datepicker>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import Datepicker from 'vue3-datepicker';

const dob = ref(null);
const dateFormat = 'yyyy-MM-dd';
</script>

<style scoped>
  .form-text-lg {
    font-size: 18px;
  }
</style>
