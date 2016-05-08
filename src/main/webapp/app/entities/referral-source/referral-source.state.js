(function() {
    'use strict';

    angular
        .module('manateeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('referral-source', {
            parent: 'entity',
            url: '/referral-source',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ReferralSources'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/referral-source/referral-sources.html',
                    controller: 'ReferralSourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('referral-source-detail', {
            parent: 'entity',
            url: '/referral-source/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ReferralSource'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/referral-source/referral-source-detail.html',
                    controller: 'ReferralSourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ReferralSource', function($stateParams, ReferralSource) {
                    return ReferralSource.get({id : $stateParams.id});
                }]
            }
        })
        .state('referral-source.new', {
            parent: 'referral-source',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/referral-source/referral-source-dialog.html',
                    controller: 'ReferralSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                contact: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('referral-source', null, { reload: true });
                }, function() {
                    $state.go('referral-source');
                });
            }]
        })
        .state('referral-source.edit', {
            parent: 'referral-source',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/referral-source/referral-source-dialog.html',
                    controller: 'ReferralSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReferralSource', function(ReferralSource) {
                            return ReferralSource.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('referral-source', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('referral-source.delete', {
            parent: 'referral-source',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/referral-source/referral-source-delete-dialog.html',
                    controller: 'ReferralSourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReferralSource', function(ReferralSource) {
                            return ReferralSource.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('referral-source', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
