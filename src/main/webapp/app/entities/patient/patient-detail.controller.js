(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('PatientDetailController', PatientDetailController);

    PatientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Patient', 'ReferralSource'];

    function PatientDetailController($scope, $rootScope, $stateParams, entity, Patient, ReferralSource) {
        var vm = this;
        vm.patient = entity;
        
        var unsubscribe = $rootScope.$on('manateeApp:patientUpdate', function(event, result) {
            vm.patient = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
