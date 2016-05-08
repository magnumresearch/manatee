(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('PatientDeleteController',PatientDeleteController);

    PatientDeleteController.$inject = ['$uibModalInstance', 'entity', 'Patient'];

    function PatientDeleteController($uibModalInstance, entity, Patient) {
        var vm = this;
        vm.patient = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Patient.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
