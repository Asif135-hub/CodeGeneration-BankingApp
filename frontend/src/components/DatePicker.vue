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

<script setup src="../assets/component-code/DatePicker.js"></script>

<style scoped src="../assets/component-code/DatePicker.css"></style>
