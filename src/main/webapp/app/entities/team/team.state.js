(function() {
    'use strict';

    angular
        .module('manateeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('team', {
            parent: 'entity',
            url: '/team',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Teams'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team/teams.html',
                    controller: 'TeamController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('team-detail', {
            parent: 'entity',
            url: '/team/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Team'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team/team-detail.html',
                    controller: 'TeamDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Team', function($stateParams, Team) {
                    return Team.get({id : $stateParams.id});
                }]
            }
        })
        .state('team.new', {
            parent: 'team',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/team-dialog.html',
                    controller: 'TeamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                organization: null,
                                name: null,
                                specialty: null,
                                maxPatients: null,
                                monday: null,
                                tuesday: null,
                                wednesday: null,
                                thursday: null,
                                friday: null,
                                saturday: null,
                                sunday: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('team', null, { reload: true });
                }, function() {
                    $state.go('team');
                });
            }]
        })
        .state('team.edit', {
            parent: 'team',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/team-dialog.html',
                    controller: 'TeamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Team', function(Team) {
                            return Team.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('team', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('team.delete', {
            parent: 'team',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/team-delete-dialog.html',
                    controller: 'TeamDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Team', function(Team) {
                            return Team.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('team', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
