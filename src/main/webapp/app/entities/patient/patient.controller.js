(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('PatientController', PatientController);

    PatientController.$inject = ['$scope', '$state', 'Patient'];

    function PatientController ($scope, $state, Patient) {
        var vm = this;
        vm.patients = [];
        vm.loadAll = function() {
            Patient.query(function(result) {
                vm.patients = result;
            });
        };

        vm.loadAll();
        
    }
})();
