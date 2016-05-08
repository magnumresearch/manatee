(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('StaffDialogController', StaffDialogController);

    StaffDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Staff', 'Team'];

    function StaffDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Staff, Team) {
        var vm = this;
        vm.staff = entity;
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('manateeApp:staffUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.staff.id !== null) {
                Staff.update(vm.staff, onSaveSuccess, onSaveError);
            } else {
                Staff.save(vm.staff, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
