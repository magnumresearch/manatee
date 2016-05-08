(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('QueueDialogController', QueueDialogController);

    QueueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Queue', 'Patient', 'Team'];

    function QueueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Queue, Patient, Team) {
        var vm = this;
        vm.queue = entity;
        vm.patients = Patient.query();
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('manateeApp:queueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.queue.id !== null) {
                Queue.update(vm.queue, onSaveSuccess, onSaveError);
            } else {
                Queue.save(vm.queue, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.timestampInitial = false;
        vm.datePickerOpenStatus.timestampFinal = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
